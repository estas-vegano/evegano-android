package com.estasvegano.android.estasvegano

import android.app.Application

import com.estasvegano.android.estasvegano.data.DataModule

import timber.log.Timber
import timber.log.Timber.DebugTree

class EVeganoApplication : Application() {

    // onCreate
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
                .builder()
                .dataModule(DataModule(this))
                .appModule(AppModule(this))
                .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
