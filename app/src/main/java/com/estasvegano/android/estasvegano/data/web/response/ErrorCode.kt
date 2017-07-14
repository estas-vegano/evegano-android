package com.estasvegano.android.estasvegano.data.web.response

import android.util.SparseArray
import com.fasterxml.jackson.annotation.JsonCreator

enum class ErrorCode private constructor(val id: Int) {

    SUCCESS(0),
    UNKNOWN(-1),
    INVALID_FORMAT(-2),
    INVALID_JSON(-3),
    MISSED_PARAMETER(-5),
    PRODUCT_NOT_FOUND(-7),
    WRONG_PARAMETER(-11),
    DUBLICATE_PRODUCT(-13),
    CATEGORY_NOT_FOUND(-17);

    companion object {

        private val errorsMap = SparseArray<ErrorCode>()

        init {
            for (code in values()) {
                errorsMap.put(code.id, code)
            }
        }

        @JsonCreator
        fun fromId(status: Int): ErrorCode {
            return errorsMap.get(status)
        }
    }
}
