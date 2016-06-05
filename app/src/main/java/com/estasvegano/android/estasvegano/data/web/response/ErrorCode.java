package com.estasvegano.android.estasvegano.data.web.response;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

    SUCCESS(0),
    UNKNOWN(-1),
    INVALID_FORMAT(-2),
    INVALID_JSON(-3),
    MISSED_PARAMETER(-5),
    PRODUCT_NOT_FOUND(-7),
    WRONG_PARAMETER(-11),
    DUBLICATE_PRIDUCT(-13),
    CATEGORY_NOT_FOUND(-17);

    @NonNull
    private static final SparseArray<ErrorCode> errorsMap = new SparseArray<>();

    static {
        for (ErrorCode code : values()) {
            errorsMap.put(code.ordinal, code);
        }
    }

    @JsonCreator
    @NonNull
    public static ErrorCode fromCode(int status) {
        return errorsMap.get(status);
    }

    private final int ordinal;

    ErrorCode(int ordinal) {
        this.ordinal = ordinal;
    }

    @JsonValue
    public int getOrdinal() {
        return ordinal;
    }
}
