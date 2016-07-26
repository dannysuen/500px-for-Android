package me.dannysuen.fivehundredpx.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by danny on 16-7-26.
 */

@Parcel
public class Photo extends Model {

    public String name;

    @SerializedName("image_url")
    public String imageUrl;

}
