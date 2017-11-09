package me.dannysuen.fivehundredpx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dannysuen.fivehundredpx.activity.BaseActivity;
import me.dannysuen.fivehundredpx.model.Category;
import me.dannysuen.fivehundredpx.util.recyclerview.DividerItemDecoration;
import me.dannysuen.fivehundredpx.util.recyclerview.ItemClickSupport;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private static final String CATEGORY_JSON_IN_ASSETS = "selected_categories.json";

    @BindView(R.id.category_list)
    RecyclerView categoriesRecyclerView;

    CategoriesAdapter categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final Gson gson = ((FiveHundredPxApplication) getApplication()).getGson();

        Observable.just(CATEGORY_JSON_IN_ASSETS)
                .map(new Func1<String, List<Category>>() {
                    @Override
                    public List<Category> call(String s) {
                        String categoriesString = loadCategoriesJsonStringFromAssets(s);
                        return gson.fromJson(categoriesString, new TypeToken<List<Category>>() {}.getType());
                    }
                })
                .filter(categories -> categories != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> setupCategoriesRecyclerView(categories));
    }

    // Load selected categories from a local json file in assets
    private String loadCategoriesJsonStringFromAssets(String filenameInAssets) {
        String json = null;
        try {
            InputStream is = getAssets().open(filenameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;

    }

    private void setupCategoriesRecyclerView(final List<Category> categories) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);
        categoriesRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        ItemClickSupport.addTo(categoriesRecyclerView).setOnItemClickListener((recyclerView, position, v) -> {
                Category category = categories.get(position);

                Intent intent = new Intent(MainActivity.this, PhotosPreviewActivity.class);
                intent.putExtra(Category.class.getCanonicalName(), Parcels.wrap(category));
                startActivity(intent);
        });

        categoriesAdapter = new CategoriesAdapter(this, categories);
        // Attach the adapter to the recyclerview to populate items
        categoriesRecyclerView.setAdapter(categoriesAdapter);
    }
}
