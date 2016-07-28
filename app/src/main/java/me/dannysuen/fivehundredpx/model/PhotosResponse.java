package me.dannysuen.fivehundredpx.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by danny on 16-7-26.
 */
public class PhotosResponse {
    /**
     * An page constant for invalid page
     */
    public static final int INVALID_PAGE = -1;

    @SerializedName("current_page")
    public int currentPage;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("total_items")
    public int totalItems;

    @SerializedName("photos")
    public List<Photo> photos;

    public boolean hasNextPage() {
        return currentPage < totalPages;
    }
}
