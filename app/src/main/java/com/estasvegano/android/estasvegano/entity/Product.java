package com.estasvegano.android.estasvegano.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_Product.Builder.class)
public abstract class Product implements Parcelable {

    @NonNull
    public static Builder builder() {
        return new AutoParcel_Product.Builder();
    }

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

    @JsonProperty("codes")
    @NonNull
    public abstract List<Code> codes();

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

        @JsonProperty("codes")
        @NonNull
        public abstract Builder codes(@NonNull List<Code> codes);

        @NonNull
        public abstract Product build();
    }

    @AutoParcel
    @JsonDeserialize(builder = AutoParcel_Product_Code.Builder.class)
    public static abstract class Code implements Parcelable {

        @NonNull
        public static Builder builder() {
            return new AutoParcel_Product_Code.Builder();
        }

        @JsonProperty("code")
        @NonNull
        public abstract String code();

        @JsonProperty("type")
        @NonNull
        public abstract String type();

        @AutoParcel.Builder
        public static abstract class Builder {

            @JsonProperty("code")
            @NonNull
            public abstract Builder code(@NonNull String code);

            @JsonProperty("type")
            @NonNull
            public abstract Builder type(@NonNull String type);

            @NonNull
            public abstract Code build();
        }
    }
}
