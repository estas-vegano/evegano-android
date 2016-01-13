package com.estasvegano.android.estasvegano.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rstk on 1/13/16.
 */
public class Photo
{
    @SerializedName("photo")
    private String photo;

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
}
