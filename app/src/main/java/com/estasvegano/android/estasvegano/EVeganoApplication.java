package com.estasvegano.android.estasvegano;

import android.app.Application;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.DataModule;

import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class EVeganoApplication extends Application {

    @SuppressWarnings("NullableProblems") // onCreate
    @NonNull
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent
                .builder()
                .dataModule(new DataModule(this))
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        }
    }

    @NonNull
    public AppComponent getComponent() {
        return component;
    }
}
