package com.estasvegano.android.estasvegano.data.web.request;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.entity.ProductType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AddProductRequest.Builder.class)
public abstract class AddProductRequest implements Parcelable {

    public static Builder builder() {
        return new AutoParcel_AddProductRequest.Builder();
    }

    @JsonProperty("title")
    @NonNull
    public abstract String title();

    @JsonProperty("info")
    @NonNull
    public abstract ProductType info();

    @JsonProperty("producer_id")
    public abstract long producerId();

    @JsonProperty("category_id")
    public abstract long categoryId();

    @JsonProperty("code")
    @NonNull
    public abstract String code();

    @JsonProperty("code_type")
    @NonNull
    public abstract String codeType();

    @SuppressWarnings("NullableProblems")
    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("title")
        @NonNull
        public abstract Builder title(@NonNull String title);

        @JsonProperty("info")
        @NonNull
        public abstract Builder info(@NonNull ProductType info);

        @JsonProperty("producer_id")
        @NonNull
        public abstract Builder producerId(long producerId);

        @JsonProperty("category_id")
        @NonNull
        public abstract Builder categoryId(long categoryId);

        @JsonProperty("title")
        @NonNull
        public abstract Builder code(@NonNull String code);

        @JsonProperty("code_type")
        @NonNull
        public abstract Builder codeType(@NonNull String codeType);

        @NonNull
        public abstract AddProductRequest build();
    }
}
