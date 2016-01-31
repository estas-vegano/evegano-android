package com.estasvegano.android.estasvegano.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.NetworkAvailabilityCheckerImpl;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.NetworkAvailabilityChecker;
import com.estasvegano.android.estasvegano.data.web.UrlConstants;
import com.estasvegano.android.estasvegano.data.web.interceptor.LocalizationInterceptor;
import com.estasvegano.android.estasvegano.data.web.interceptor.LoggingInterceptor;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

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
        return new LoggingInterceptor(Logger.getLogger("EVEGANO DATA"));
    }

    @Provides
    @NonNull
    LocalizationInterceptor getLocalizationInterceptor() {
        return new LocalizationInterceptor();
    }

    @Singleton
    @Provides
    @NonNull
    EVeganoApi getWebApi(
            @NonNull LoggingInterceptor loggingInterceptor,
            @NonNull LocalizationInterceptor localizationInterceptor
    ) {
        OkHttpClient client = new OkHttpClient();
        List<Interceptor> interceptors = client.interceptors();
        interceptors.add(localizationInterceptor);
        interceptors.add(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(UrlConstants.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(EVeganoApi.class);
    }
}
