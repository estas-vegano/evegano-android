package com.estasvegano.android.estasvegano;

import android.content.Context;
import android.net.ConnectivityManager;

import com.estasvegano.android.estasvegano.data.web.NetworkAvailabilityChecker;

/**
 * Created by rstk on 1/13/16.
 */
public class NetworkAvailabilityCheckerImpl implements NetworkAvailabilityChecker
{
    private final Context context;

    public NetworkAvailabilityCheckerImpl(Context context)
    {
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean isNetworkEnabled()
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }
}
