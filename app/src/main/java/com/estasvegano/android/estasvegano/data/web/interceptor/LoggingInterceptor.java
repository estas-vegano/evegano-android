package com.estasvegano.android.estasvegano.data.web.interceptor;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.util.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Locale;


public class LoggingInterceptor implements Interceptor {
    @NonNull
    private final Logger logger;

    public LoggingInterceptor(@NonNull Logger logger) {
        this.logger = logger;
    }

    @Override
    @NonNull
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        logger.info("Sending request %s %s",
                request.url(),
                String.format("%n%s", request.headers())
        );

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        logger.info("Received response for %s in %n%s",
                response.request().url(),
                String.format(Locale.US, "%.1fms", (t2 - t1) / 1e6d),
                String.format("%n%s", response.headers())
        );

        return response;
    }
}
