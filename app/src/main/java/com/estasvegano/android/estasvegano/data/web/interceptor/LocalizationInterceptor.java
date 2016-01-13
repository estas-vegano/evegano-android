package com.estasvegano.android.estasvegano.data.web.interceptor;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by rstk on 1/13/16.
 */
public class LocalizationInterceptor implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Accept-Language", Locale.getDefault().getCountry())
                .build();

        Response response = chain.proceed(request);

        return response;
    }
}
