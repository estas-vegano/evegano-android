package com.estasvegano.android.estasvegano

import android.content.Context
import android.net.ConnectivityManager

import com.estasvegano.android.estasvegano.data.web.NetworkAvailabilityChecker

class NetworkAvailabilityCheckerImpl(context: Context) : NetworkAvailabilityChecker {

    private val context: Context = context.applicationContext

    override fun isNetworkEnabled(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
                && cm.activeNetworkInfo.isAvailable
                && cm.activeNetworkInfo.isConnected
    }
}
