package me.dannysuen.fivehundredpx;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dannysuen.fivehundredpx.model.Photo;

/**
 * Created by danny on 16-7-26.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail_image)
        ImageView mThumbnailImage;

        @BindView(R.id.name_text)
        TextView mNameText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Photo> mPhotos;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_item_photo, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);

        Picasso.with(mContext).load(photo.imageUrl).into(holder.mThumbnailImage);

        holder.mNameText.setText(photo.name);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }
}
