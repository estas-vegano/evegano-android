package com.estasvegano.android.estasvegano.entity;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_Complain.Builder.class)
public abstract class Complain {

    @NonNull
    public static Builder builder() {
        return new AutoParcel_Complain.Builder();
    }

    @JsonProperty("message")
    @NonNull
    public abstract String message();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("message")
        @NonNull
        public abstract Builder message(@NonNull String message);

        @NonNull
        public abstract Complain build();
    }
}