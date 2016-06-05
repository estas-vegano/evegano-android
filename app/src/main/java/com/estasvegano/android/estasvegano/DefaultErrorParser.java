package com.estasvegano.android.estasvegano;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.ApiException;
import com.estasvegano.android.estasvegano.data.web.ErrorParser;
import com.estasvegano.android.estasvegano.data.web.response.BaseResponse;
import com.estasvegano.android.estasvegano.data.web.response.ErrorCode;

import rx.Single;

import static rx.Single.error;
import static rx.Single.just;

public class DefaultErrorParser implements ErrorParser {

    @NonNull
    public <T> Single<T> checkIfError(@NonNull BaseResponse<T> response) {
        //noinspection ConstantConditions
        return response.errorCode() != ErrorCode.SUCCESS
                ? error(new ApiException(response.errorCode(), response.errorMessage()))
                : just(response.result());
    }
}
