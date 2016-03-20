package com.estasvegano.android.estasvegano.data.web.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class LocalizationInterceptor implements Interceptor
{
    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException
    {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Accept-Language", Locale.getDefault().getCountry())
                .build();

        Response response = chain.proceed(request);

        return response;
    }
}
