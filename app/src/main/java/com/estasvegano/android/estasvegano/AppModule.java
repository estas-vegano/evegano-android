package com.estasvegano.android.estasvegano;

import com.estasvegano.android.estasvegano.data.ErrorParser;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    ErrorParser getErrorParser() {
        return new DefaultErrorParser();
    }
}
