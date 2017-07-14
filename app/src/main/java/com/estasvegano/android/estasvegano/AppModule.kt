package com.estasvegano.android.estasvegano

import android.content.Context

import com.estasvegano.android.estasvegano.data.web.ErrorParser
import com.squareup.picasso.Picasso

import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    internal fun errorParser() = ErrorParser()

    @Provides
    internal fun picasso(): Picasso {
        val picasso = Picasso.with(context)
        picasso.isLoggingEnabled = BuildConfig.DEBUG
        return picasso
    }
}
