package com.estasvegano.android.estasvegano.data

import android.content.Context
import com.estasvegano.android.estasvegano.BuildConfig
import com.estasvegano.android.estasvegano.NetworkAvailabilityCheckerImpl
import com.estasvegano.android.estasvegano.data.web.EVeganoApi
import com.estasvegano.android.estasvegano.data.web.UrlConstants
import com.estasvegano.android.estasvegano.data.web.interceptor.LocalizationInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class DataModule(private val context: Context) {

    @Provides
    internal fun provideNetworkAbilityChecker() = NetworkAvailabilityCheckerImpl(context)

    @Provides
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor({ Timber.d(it) })
        loggingInterceptor.level = if (BuildConfig.DEBUG) HEADERS else NONE
        return loggingInterceptor
    }

    @Provides
    internal fun provideLocalizationInterceptor() = LocalizationInterceptor()

    @Provides
    internal fun provideObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
    }

    @Singleton
    @Provides
    internal fun provideWebApi(
            loggingInterceptor: HttpLoggingInterceptor,
            localizationInterceptor: LocalizationInterceptor,
            objectMapper: ObjectMapper
    ): EVeganoApi {
        val client = OkHttpClient()
                .newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(localizationInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(UrlConstants.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(EVeganoApi::class.java)
    }
}
