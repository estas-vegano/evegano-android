package com.estasvegano.android.estasvegano.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rstk on 1/13/16.
 */
public class Producer
{
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("ethical")
    private boolean ethical;

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

    public boolean isEthical()
    {
        return ethical;
    }

    public void setEthical(boolean ethical)
    {
        this.ethical = ethical;
    }
}