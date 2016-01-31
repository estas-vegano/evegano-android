package com.estasvegano.android.estasvegano.data;

public interface ErrorParser {
    int NO_ERROR_STATUS = -1;

    int getErrorStatus(Throwable error);
}
