package com.estasvegano.android.estasvegano.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_Producer.Builder.class)
public abstract class Producer implements Parcelable {

    public static Builder builder() {
        return new AutoParcel_Producer.Builder();
    }

    @JsonProperty("id")
    public abstract long id();

    @JsonProperty("title")
    @NonNull
    public abstract String title();

    @JsonProperty("ethical")
    @Nullable
    public abstract Boolean ethical();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("id")
        @NonNull
        public abstract Builder id(long id);

        @JsonProperty("title")
        @NonNull
        public abstract Builder title(@NonNull String title);

        @JsonProperty("ethical")
        @NonNull
        public abstract Builder ethical(@Nullable Boolean ethical);

        @NonNull
        public abstract Producer build();
    }
}