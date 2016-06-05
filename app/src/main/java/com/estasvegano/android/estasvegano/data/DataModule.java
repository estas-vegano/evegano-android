package com.estasvegano.android.estasvegano.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.BuildConfig;
import com.estasvegano.android.estasvegano.NetworkAvailabilityCheckerImpl;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.NetworkAvailabilityChecker;
import com.estasvegano.android.estasvegano.data.web.UrlConstants;
import com.estasvegano.android.estasvegano.data.web.interceptor.LocalizationInterceptor;
import com.estasvegano.android.estasvegano.data.web.interceptor.LoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;

import static com.estasvegano.android.estasvegano.data.web.interceptor.LoggingInterceptor.Level.BODY;
import static com.estasvegano.android.estasvegano.data.web.interceptor.LoggingInterceptor.Level.NONE;

@Module
public class DataModule {

    @NonNull
    private final Context context;

    public DataModule(@NonNull Context context) {
        this.context = context;
    }

    @Provides
    @NonNull
    NetworkAvailabilityChecker getNetworkAbilityChecker() {
        return new NetworkAvailabilityCheckerImpl(context);
    }

    @Provides
    @NonNull
    LoggingInterceptor getLoggingInterceptor() {
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor(Timber::i);
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
        return loggingInterceptor;
    }

    @Provides
    @NonNull
    LocalizationInterceptor getLocalizationInterceptor() {
        return new LocalizationInterceptor();
    }

    @Provides
    @NonNull
    ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }

    @Singleton
    @Provides
    @NonNull
    EVeganoApi getWebApi(
            @NonNull LoggingInterceptor loggingInterceptor,
            @NonNull LocalizationInterceptor localizationInterceptor,
            @NonNull ObjectMapper objectMapper
    ) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(localizationInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(UrlConstants.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(EVeganoApi.class);
    }
}
