package com.estasvegano.android.estasvegano.entity;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_Photo.Builder.class)
public abstract class Photo {

    @NonNull
    public static Builder builder() {
        return new AutoParcel_Photo.Builder();
    }

    @JsonProperty("url")
    @NonNull
    public abstract String photo();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("url")
        @NonNull
        public abstract Builder photo(@NonNull String photo);

        @NonNull
        public abstract Photo build();
    }
}
