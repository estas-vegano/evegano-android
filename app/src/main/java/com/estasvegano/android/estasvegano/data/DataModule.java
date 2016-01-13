package com.estasvegano.android.estasvegano.data;

import android.content.Context;

import com.estasvegano.android.estasvegano.NetworkAvailabilityCheckerImpl;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.NetworkAvailabilityChecker;
import com.estasvegano.android.estasvegano.data.web.UppercaseEnumGsonAdapter;
import com.estasvegano.android.estasvegano.data.web.UrlConstants;
import com.estasvegano.android.estasvegano.data.web.interceptor.LocalizationInterceptor;
import com.estasvegano.android.estasvegano.data.web.interceptor.LoggingInterceptor;
import com.estasvegano.android.estasvegano.model.ProductType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.logging.Logger;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by rstk on 1/13/16.
 */
@Module
public class DataModule
{
    private final Context context;

    public DataModule(Context context)
    {
        this.context = context;
    }

    @Provides
    NetworkAvailabilityChecker getNetworkAbilityChecker()
    {
        return new NetworkAvailabilityCheckerImpl(context);
    }

    @Provides
    LoggingInterceptor getLoggingInterceptor()
    {
        return new LoggingInterceptor(Logger.getLogger("EVEGANO DATA"));
    }

    @Provides
    Gson getGson()
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ProductType.class, new UppercaseEnumGsonAdapter())
                .create();
        return gson;
    }

    @Provides
    EVeganoApi getWebApi(Gson gson,
                         LoggingInterceptor loggingInterceptor,
                         LocalizationInterceptor localizationInterceptor)
    {
        OkHttpClient client = new OkHttpClient();
        List<Interceptor> interceptors = client.interceptors();
        interceptors.add(localizationInterceptor);
        interceptors.add(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(UrlConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(EVeganoApi.class);
    }
}
