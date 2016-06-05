package com.estasvegano.android.estasvegano.model;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.AppModule;
import com.estasvegano.android.estasvegano.data.DataModule;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.ErrorParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;

@Module(includes = {DataModule.class, AppModule.class})
public class ModelModule {

    @Provides
    @NonNull
    ProductModel getProductModel(
            @NonNull EVeganoApi api,
            @NonNull ErrorParser errorParser,
            @NonNull ObjectMapper objectMapper
    ) {
        return new ProductModel(api, errorParser, objectMapper);
    }

    @Provides
    @NonNull
    CategoryModel getCategoryModel(
            @NonNull EVeganoApi api,
            @NonNull ErrorParser errorParser,
            @NonNull ObjectMapper objectMapper
    ) {
        return new CategoryModel(api, errorParser, objectMapper);
    }


    @Provides
    @NonNull
    ProducerModel getProducerModel(
            @NonNull EVeganoApi api,
            @NonNull ErrorParser errorParser,
            @NonNull ObjectMapper objectMapper
    ) {
        return new ProducerModel(api, errorParser, objectMapper);
    }
}
