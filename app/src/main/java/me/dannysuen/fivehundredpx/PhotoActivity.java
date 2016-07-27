package me.dannysuen.fivehundredpx;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.parceler.Parcels;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dannysuen.fivehundredpx.activity.BaseActivity;
import me.dannysuen.fivehundredpx.model.Photo;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoActivity extends BaseActivity {

    private Photo mPhoto;
    PhotoViewAttacher mAttacher;

    @BindView(R.id.photo_image)
    ImageView mPhotoImage;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPhoto = Parcels.unwrap(getIntent().getParcelableExtra(Photo.class.getCanonicalName()));
        setTitle(mPhoto.name);

        Picasso.with(this).load(mPhoto.imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mPhotoImage.setImageBitmap(bitmap);

                mPhotoImage.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mAttacher = new PhotoViewAttacher(mPhotoImage);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

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
}
