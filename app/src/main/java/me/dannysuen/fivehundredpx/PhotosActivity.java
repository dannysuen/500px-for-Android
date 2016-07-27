package me.dannysuen.fivehundredpx;

import org.parceler.Parcels;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dannysuen.fivehundredpx.api.PhotosApiService;
import me.dannysuen.fivehundredpx.model.Category;
import me.dannysuen.fivehundredpx.model.Envelop;
import me.dannysuen.fivehundredpx.model.Photo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotosActivity extends AppCompatActivity {

    private static final int INVALID_PAGE = -1;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.users_list)
    RecyclerView mUsersRecyclerView;
    private Retrofit mRetrofit;

    private PhotosApiService mService;

    private PhotosAdapter mAdapter;

    private int mNextPage = 1;

    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCategory = Parcels.unwrap(getIntent().getParcelableExtra(Category.class.getCanonicalName()));
        setTitle(mCategory.name);

        mRetrofit = ((FiveHundredPxApplication) getApplication()).getRetrofit();
        mService = mRetrofit.create(PhotosApiService.class);

        // Set layout manager to position the items
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mUsersRecyclerView.setLayoutManager(linearLayoutManager);
        mUsersRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        mUsersRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                loadMoreFromApi(mNextPage);
            }
        });
        ItemClickSupport.addTo(mUsersRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        List<Photo> photos = ((PhotosAdapter) recyclerView.getAdapter()).getPhotos();
                        Photo photo = photos.get(position);

                        Intent intent = new Intent(PhotosActivity.this, PhotoActivity.class);
                        intent.putExtra(Photo.class.getCanonicalName(), Parcels.wrap(photo));
                        startActivity(intent);
                    }
                }
        );

        // It needs to be put into MessageQueue in order to animate the control
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });

        fetchPhotosList();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPhotosList();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchPhotosList() {
        mNextPage = 1;

        Call<Envelop> call = mService.fetchPhotos(Constants.CONSUMER_KEY, mCategory.name, mNextPage);
        call.enqueue(new Callback<Envelop>() {
            @Override
            public void onResponse(Call<Envelop> call, Response<Envelop> response) {
                if (response.isSuccessful()) {
                    Envelop envelop = response.body();

//                    mNextPage = envelop.pagination.nextUrl == null ? INVALID_PAGE : mNextPage + 1;

                    // Create adapter passing in the sample user data
                    mAdapter = new PhotosAdapter(PhotosActivity.this, envelop.photos);
                    // Attach the adapter to the recyclerview to populate items
                    mUsersRecyclerView.setAdapter(mAdapter);
                } else {
//                    Toast.makeText(PhotosActivity.this, R.string.fetch_users_failed, Toast.LENGTH_SHORT)
//                            .show();
                }

                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Envelop> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}
