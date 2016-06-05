package com.estasvegano.android.estasvegano.data.web.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_BaseResponse.Builder.class)
public abstract class BaseResponse<T> {

    @JsonProperty("error_message")
    @Nullable
    public abstract String errorMessage();

    @JsonProperty("error_code")
    @NonNull
    public abstract ErrorCode errorCode();

    @JsonProperty("result")
    @Nullable
    public abstract T result();

    public Builder builder() {
        return new AutoParcel_BaseResponse.Builder();
    }

    @AutoParcel.Builder
    public static abstract class Builder<T> {

        @JsonProperty("error_message")
        @NonNull
        public abstract Builder<T> errorMessage(@Nullable String errorMessage);

        @JsonProperty("error_code")
        public abstract Builder<T> errorCode(@NonNull ErrorCode errorCode);

        @JsonProperty("result")
        @NonNull
        public abstract Builder<T> result(@NonNull T result);

        @NonNull
        public abstract BaseResponse<T> build();

    }
}
