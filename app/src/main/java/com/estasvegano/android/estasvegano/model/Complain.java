package com.estasvegano.android.estasvegano.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rstk on 1/13/16.
 */
public class Complain
{

    @SerializedName("message")
    private String message;

    public Complain(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
