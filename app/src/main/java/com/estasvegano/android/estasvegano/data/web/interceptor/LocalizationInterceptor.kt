package com.estasvegano.android.estasvegano.data.web.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class LocalizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
                .addHeader("Accept-Language", Locale.getDefault().country)
                .build()

        return chain.proceed(request)
    }
}
