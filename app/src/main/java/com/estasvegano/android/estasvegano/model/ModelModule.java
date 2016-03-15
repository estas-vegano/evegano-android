package com.estasvegano.android.estasvegano.model;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.AppModule;
import com.estasvegano.android.estasvegano.data.DataModule;
import com.estasvegano.android.estasvegano.data.ErrorParser;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;

import dagger.Module;
import dagger.Provides;

@Module(includes = {DataModule.class, AppModule.class})
public class ModelModule {

    @Provides
    @NonNull
    ProductModel getProductModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser) {
        return new ProductModel(api, errorParser);
    }

    @Provides
    @NonNull
    CategoryModel getCategoryModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser) {
        return new CategoryModel(api, errorParser);
    }

    @Provides
    @NonNull
    ProducerModel getProducerModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser) {
        return new ProducerModel(api, errorParser);
    }
}
