package com.estasvegano.android.estasvegano.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_Category.Builder.class)
public abstract class Category implements Parcelable {

    @NonNull
    public static Builder builder() {
        return new AutoParcel_Category.Builder();
    }

    @NonNull
    public abstract Builder toBuilder();

    @JsonProperty("id")
    @NonNull
    public abstract long id();

    @JsonProperty("title")
    @NonNull
    public abstract String title();

    @JsonProperty("children")
    @Nullable
    public abstract List subCategories();

    @JsonProperty("parent")
    @Nullable
    public abstract Category parent();

    public boolean isLowLevel() {
        return subCategories() == null || subCategories().size() == 0;
    }

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("id")
        @NonNull
        public abstract Builder id(long id);

        @JsonProperty("title")
        @NonNull
        public abstract Builder title(@NonNull String title);

        @JsonProperty("children")
        @NonNull
        public abstract Builder subCategories(@Nullable List subCategories);

        @JsonProperty("parent")
        @NonNull
        public abstract Builder parent(@Nullable Category parent);

        @NonNull
        public abstract Category build();
    }
}
