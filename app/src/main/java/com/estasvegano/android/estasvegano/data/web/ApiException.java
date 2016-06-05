package com.estasvegano.android.estasvegano.data.web;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.response.ErrorCode;

public class ApiException extends RuntimeException {

    @NonNull
    private final ErrorCode errorCode;

    @NonNull
    private final String message;

    public ApiException(@NonNull ErrorCode errorCode, @NonNull String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @NonNull
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    @NonNull
    public String getMessage() {
        return message;
    }
}
