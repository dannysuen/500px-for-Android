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
import me.dannysuen.fivehundredpx.model.Category;


/**
 * Created by danny on 16-7-26.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.cover_image)
//        ImageView coverImage;

        @BindView(R.id.name_text)
        TextView nameText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Category> mCategories;
    private Context mContext;

    public CategoriesAdapter(Context context, List<Category> categories) {
        mContext = context;
        mCategories = categories;
    }

    public void addAll(List<Category> categories) {
        int size = getItemCount();
        mCategories.addAll(categories);
        notifyItemRangeInserted(size, getItemCount());
    }

    public void clear() {
        mCategories.clear();
        notifyDataSetChanged();
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_item_category, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = mCategories.get(position);

//        Picasso.with(mContext).load(category.coverImageUrl).into(holder.coverImage);

        holder.nameText.setText(category.name);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }


}
