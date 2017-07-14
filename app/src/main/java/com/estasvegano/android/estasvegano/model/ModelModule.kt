package com.estasvegano.android.estasvegano.model

import com.estasvegano.android.estasvegano.AppModule
import com.estasvegano.android.estasvegano.data.DataModule
import com.estasvegano.android.estasvegano.data.web.EVeganoApi
import com.estasvegano.android.estasvegano.data.web.ErrorParser
import com.fasterxml.jackson.databind.ObjectMapper

import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(DataModule::class, AppModule::class))
class ModelModule {

    @Provides
    internal fun provideProductModel(
            api: EVeganoApi,
            errorParser: ErrorParser,
            objectMapper: ObjectMapper
    ): ProductModel {
        return ProductModel(api, errorParser, objectMapper)
    }

    @Provides
    internal fun provideCategoryModel(
            api: EVeganoApi,
            errorParser: ErrorParser,
            objectMapper: ObjectMapper
    ): CategoryModel {
        return CategoryModel(api, errorParser, objectMapper)
    }


    @Provides
    internal fun provideProducerModel(
            api: EVeganoApi,
            errorParser: ErrorParser,
            objectMapper: ObjectMapper
    ): ProducerModel {
        return ProducerModel(api, errorParser, objectMapper)
    }
}
