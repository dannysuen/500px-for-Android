package me.dannysuen.fivehundredpx.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danny on 16-7-26.
 */

@Parcel
public class Category extends Model {

    @ParcelConstructor
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Category name: like Animals, Nature...
     */
    public String name;

    // Selected cover image url
    @SerializedName("cover_image_url")
    public String coverImageUrl;

}
