package me.dannysuen.fivehundredpx;

import org.parceler.Parcels;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dannysuen.fivehundredpx.activity.BaseActivity;
import me.dannysuen.fivehundredpx.api.PhotosApiService;
import me.dannysuen.fivehundredpx.model.Category;
import me.dannysuen.fivehundredpx.model.PhotosResponse;
import me.dannysuen.fivehundredpx.model.Photo;
import me.dannysuen.fivehundredpx.util.recyclerview.DividerItemDecoration;
import me.dannysuen.fivehundredpx.util.recyclerview.EndlessRecyclerViewScrollListener;
import me.dannysuen.fivehundredpx.util.recyclerview.ItemClickSupport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotosPreviewActivity extends BaseActivity {

    private static final int INVALID_PAGE = -1;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.users_list)
    RecyclerView mPhotosRecyclerView;

    private Retrofit mRetrofit;

    private PhotosApiService mService;

    private PhotosAdapter mAdapter;

    private int mNextPage = 1;

    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_preview);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCategory = Parcels.unwrap(getIntent().getParcelableExtra(Category.class.getCanonicalName()));
        setTitle(mCategory.name);

        mRetrofit = ((FiveHundredPxApplication) getApplication()).getRetrofit();
        mService = mRetrofit.create(PhotosApiService.class);

        setupPhotosRecyclerView();

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

    private void setupPhotosRecyclerView() {
        // Set layout manager to position the items
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPhotosRecyclerView.setLayoutManager(linearLayoutManager);
        mPhotosRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        mPhotosRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextPage(mNextPage);
            }
        });
        ItemClickSupport.addTo(mPhotosRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        List<Photo> photos = ((PhotosAdapter) recyclerView.getAdapter()).getPhotos();
                        Photo photo = photos.get(position);

                        Intent intent = new Intent(PhotosPreviewActivity.this, PhotoActivity.class);
                        intent.putExtra(Photo.class.getCanonicalName(), Parcels.wrap(photo));
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    private void fetchPhotosList() {
        mNextPage = 1;

        Call<PhotosResponse> call = mService.fetchPhotos(Constants.CONSUMER_KEY, mCategory.name, mNextPage,
                Photo.DEFAULT_IMAGE_SIZE_ID);
        call.enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                if (response.isSuccessful()) {
                    PhotosResponse envelop = response.body();

                    mNextPage = !envelop.hasNextPage() ? INVALID_PAGE : mNextPage + 1;

                    // Create adapter passing in the sample user data
                    mAdapter = new PhotosAdapter(PhotosPreviewActivity.this, envelop.photos);
                    // Attach the adapter to the recyclerview to populate items
                    mPhotosRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(PhotosPreviewActivity.this, R.string.fetch_photos_failed, Toast.LENGTH_SHORT)
                            .show();
                }

                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadNextPage(int page) {
        if (page != INVALID_PAGE) {
            Call<PhotosResponse> call = mService.fetchPhotos(Constants.CONSUMER_KEY, mCategory.name, page,
                    Photo.DEFAULT_IMAGE_SIZE_ID);
            call.enqueue(new Callback<PhotosResponse>() {
                @Override
                public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse>
                        response) {
                    if (response.isSuccessful()) {
                        PhotosResponse envelop = response.body();
                        int size = mAdapter.getItemCount();
                        mAdapter.addAll(envelop.photos);
//                        mAdapter.notifyItemRangeInserted(size, envelop.photos.size());

                        mNextPage = !envelop.hasNextPage() ? INVALID_PAGE : mNextPage + 1;
                    }
                }

                @Override
                public void onFailure(Call<PhotosResponse> call, Throwable t) {

                }
            });
        } else {

        }
    }
}
