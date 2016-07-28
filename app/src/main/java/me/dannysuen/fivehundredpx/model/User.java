package me.dannysuen.fivehundredpx.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Represents a user model class.
 */
@Parcel
public class User extends Model {

    public String username;

    public String country;

    @SerializedName("fullname")
    public String fullName;
}
