package com.estasvegano.android.estasvegano.data.web

import com.estasvegano.android.estasvegano.data.web.response.BaseResponse
import com.estasvegano.android.estasvegano.data.web.response.ErrorCode
import io.reactivex.Single

class ErrorParser {

    fun checkIfError(response: BaseResponse): Single<Any> {
        return if (response.errorCode != ErrorCode.SUCCESS)
            Single.error<Any>(ApiException(response.errorCode, response.errorMessage))
        else
            Single.just(response.result ?: Unit)
    }
}
