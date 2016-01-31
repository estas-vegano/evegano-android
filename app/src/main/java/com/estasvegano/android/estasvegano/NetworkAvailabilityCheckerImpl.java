package com.estasvegano.android.estasvegano;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.NetworkAvailabilityChecker;


public class NetworkAvailabilityCheckerImpl implements NetworkAvailabilityChecker {
    @NonNull
    private final Context context;

    public NetworkAvailabilityCheckerImpl(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean isNetworkEnabled() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }
}
