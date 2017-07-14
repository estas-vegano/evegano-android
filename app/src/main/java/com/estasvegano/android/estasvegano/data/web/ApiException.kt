package com.estasvegano.android.estasvegano.data.web

import com.estasvegano.android.estasvegano.data.web.response.ErrorCode

class ApiException(val errorCode: ErrorCode, override val message: String?)
    : RuntimeException("Api error: $message")
