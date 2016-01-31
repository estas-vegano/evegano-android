package com.estasvegano.android.estasvegano;

import com.estasvegano.android.estasvegano.data.ErrorParser;

import retrofit.HttpException;

public class DefaultErrorParser implements ErrorParser {

    @Override
    public int getErrorStatus(Throwable error) {
        if (error instanceof HttpException) {
            return ((HttpException) error).code();
        }
        return NO_ERROR_STATUS;
    }
}
