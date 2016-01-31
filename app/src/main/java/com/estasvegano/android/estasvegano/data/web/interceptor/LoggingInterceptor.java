package com.estasvegano.android.estasvegano.data.web.interceptor;

import android.support.annotation.NonNull;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.logging.Logger;


public class LoggingInterceptor implements Interceptor
{
    @NonNull
    private final Logger logger;

    public LoggingInterceptor(@NonNull Logger logger)
    {
        this.logger = logger;
    }

    @Override
    @NonNull
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException
    {
        Request request = chain.request();

        long t1 = System.nanoTime();
        logger.info(String.format("Sending request %s %n%s",
                request.url(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        logger.info(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
