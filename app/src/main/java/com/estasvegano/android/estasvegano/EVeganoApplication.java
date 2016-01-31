package com.estasvegano.android.estasvegano;

import android.app.Application;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.DataModule;

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
                .build();
    }

    @NonNull
    public AppComponent getComponent() {
        return component;
    }
}
