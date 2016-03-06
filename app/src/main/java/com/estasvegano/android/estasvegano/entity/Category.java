package com.estasvegano.android.estasvegano.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_Category.Builder.class)
public abstract class Category implements Parcelable {

    @NonNull
    public static Builder builder() {
        return new AutoParcel_Category.Builder();
    }

    @JsonProperty("id")
    @NonNull
    public abstract long id();

    @JsonProperty("title")
    @NonNull
    public abstract String title();

    @JsonProperty("sub_category")
    @Nullable
    public abstract Category subCategory();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("id")
        @NonNull
        public abstract Builder id(long id);

        @JsonProperty("title")
        @NonNull
        public abstract Builder title(@NonNull String title);

        @JsonProperty("sub_category")
        @NonNull
        public abstract Builder subCategory(@Nullable Category subCategory);

        @NonNull
        public abstract Category build();
    }
}
