package com.estasvegano.android.estasvegano.data.web;

import com.estasvegano.android.estasvegano.data.web.response.BaseResponse;

import rx.Single;

public interface ErrorParser {

    <T> Single<T> checkIfError(BaseResponse<T> baseResponse);
}
