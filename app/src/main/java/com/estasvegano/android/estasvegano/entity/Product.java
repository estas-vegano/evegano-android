package com.estasvegano.android.estasvegano.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = Product.Builder.class)
public abstract class Product implements Parcelable {

    @JsonProperty("id")
    public abstract long id();

    @JsonProperty("title")
    @NonNull
    public abstract String title();

    @JsonProperty("info")
    @NonNull
    public abstract ProductType info();

    @JsonProperty("photo")
    @Nullable
    public abstract String photo();

    @JsonProperty("producer")
    @NonNull
    public abstract Producer producer();

    @JsonProperty("category")
    @NonNull
    public abstract Category category();

    @SuppressWarnings("NullableProblems")
    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("id")
        @NonNull
        public abstract Builder id(long id);

        @JsonProperty("title")
        @NonNull
        public abstract Builder title(@NonNull String title);

        @JsonProperty("info")
        @NonNull
        public abstract Builder info(@NonNull ProductType info);

        @JsonProperty("photo")
        @NonNull
        public abstract Builder photo(@NonNull String photo);

        @JsonProperty("producer")
        @NonNull
        public abstract Builder producer(@NonNull Producer producer);

        @JsonProperty("category")
        @NonNull
        public abstract Builder category(@NonNull Category category);

        @NonNull
        public abstract Product build();
    }
}
