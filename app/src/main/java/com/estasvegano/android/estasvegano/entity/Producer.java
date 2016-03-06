package com.estasvegano.android.estasvegano.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = Producer.Builder.class)
public abstract class Producer implements Parcelable {

    public static Builder builder() {
        return new AutoParcel_Producer.Builder();
    }

    @JsonProperty("id")
    public abstract long id();

    @JsonProperty("title")
    public abstract String title();

    @JsonProperty("ethical")
    public abstract boolean ethical();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("id")
        public abstract Builder id(long id);

        @JsonProperty("title")
        public abstract Builder title(@NonNull String title);

        @JsonProperty("ethical")
        public abstract Builder ethical(boolean ethical);

        @NonNull
        public abstract Producer build();
    }
}