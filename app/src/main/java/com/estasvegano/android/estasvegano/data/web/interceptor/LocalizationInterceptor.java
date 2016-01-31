package com.estasvegano.android.estasvegano.data.web.interceptor;

import android.support.annotation.NonNull;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Locale;


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
