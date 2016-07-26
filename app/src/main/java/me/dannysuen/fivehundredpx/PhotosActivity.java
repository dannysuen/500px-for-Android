package me.dannysuen.fivehundredpx;

import org.parceler.Parcels;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dannysuen.fivehundredpx.api.PhotosApiService;
import me.dannysuen.fivehundredpx.model.Photo;
import retrofit2.Retrofit;

public class PhotosActivity extends AppCompatActivity {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.users_list)
    RecyclerView mUsersRecyclerView;
    private Retrofit mRetrofit;

    private PhotosApiService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);

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
    }
}
