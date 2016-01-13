package com.estasvegano.android.estasvegano.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rstk on 1/13/16.
 */
public class Category
{
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("sub_category")
    private Category subCategory;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Nullable
    public Category getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory(Category subCategory)
    {
        this.subCategory = subCategory;
    }
}
