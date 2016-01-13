package com.estasvegano.android.estasvegano.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rstk on 1/13/16.
 */
public class Product
{
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("info")
    private ProductType info;
    @SerializedName("photo")
    private String photo;
    @SerializedName("producer")
    private Producer producer;
    @SerializedName("category")
    private Category category;

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

    public ProductType getInfo()
    {
        return info;
    }

    public void setInfo(ProductType info)
    {
        this.info = info;
    }

    @Nullable
    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public Producer getProducer()
    {
        return producer;
    }

    public void setProducer(Producer producer)
    {
        this.producer = producer;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }
}
