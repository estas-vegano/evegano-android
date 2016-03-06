package com.estasvegano.android.estasvegano;

import android.content.Context;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.ErrorParser;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @NonNull
    private final Context context;

    public AppModule(@NonNull Context context) {
        this.context = context;
    }

    @Provides
    ErrorParser getErrorParser() {
        return new DefaultErrorParser();
    }

    @Provides
    Picasso getPicasso() {
        Picasso picasso = Picasso.with(context);
        picasso.setLoggingEnabled(BuildConfig.DEBUG);
        return picasso;
    }
}
