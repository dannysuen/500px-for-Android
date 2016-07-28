package me.dannysuen.fivehundredpx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dannysuen.fivehundredpx.activity.BaseActivity;
import me.dannysuen.fivehundredpx.model.Category;
import me.dannysuen.fivehundredpx.util.recyclerview.DividerItemDecoration;
import me.dannysuen.fivehundredpx.util.recyclerview.ItemClickSupport;

public class MainActivity extends BaseActivity {

    @BindView(R.id.category_list)
    RecyclerView mCategoriesRecyclerView;

    CategoriesAdapter mAdapter;
    List<Category> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Gson gson = ((FiveHundredPxApplication) getApplication()).getGson();
        // Load selected categories from a local json file in assets

        String categoriesString = loadCategoriesJsonStringFromAssets();
        if (!TextUtils.isEmpty(categoriesString)) {
            mCategories = gson.fromJson(categoriesString, new TypeToken<List<Category>>() {}.getType());
            setupCategoriesRecyclerView();
        }
    }

    public String loadCategoriesJsonStringFromAssets() {
        String json = null;
        try {
            InputStream is = getAssets().open("selected_categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private void setupCategoriesRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCategoriesRecyclerView.setLayoutManager(linearLayoutManager);
        mCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        ItemClickSupport.addTo(mCategoriesRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Category category = mCategories.get(position);

                        Intent intent = new Intent(MainActivity.this, PhotosPreviewActivity.class);
                        intent.putExtra(Category.class.getCanonicalName(), Parcels.wrap(category));
                        startActivity(intent);
                    }
                }
        );

        mAdapter = new CategoriesAdapter(this, mCategories);
        // Attach the adapter to the recyclerview to populate items
        mCategoriesRecyclerView.setAdapter(mAdapter);
    }
}
