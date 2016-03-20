package com.estasvegano.android.estasvegano.data.web.response;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_CategoriesResponse.Builder.class)
public abstract class CategoriesResponse implements Parcelable {

    @NonNull
    public static Builder builder() {
        return new AutoParcel_CategoriesResponse.Builder();
    }

    @JsonProperty("categories")
    @NonNull
    public abstract List<Category> subCategories();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("categories")
        @NonNull
        public abstract Builder subCategories(@NonNull List<Category> subCategories);

        @NonNull
        public abstract CategoriesResponse build();
    }
}
