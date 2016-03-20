package com.estasvegano.android.estasvegano.data.web.request;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AddProducerRequest.Builder.class)
public abstract class AddProducerRequest implements Parcelable {

    public static Builder builder() {
        return new AutoParcel_AddProducerRequest.Builder().ethical(null);
    }

    @JsonProperty("title")
    public abstract String title();


    @JsonProperty("ethical")
    @Nullable
    public abstract Boolean ethical();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("title")
        public abstract Builder title(@NonNull String title);

        @JsonProperty("ethical")
        public abstract Builder ethical(@Nullable Boolean ethical);

        @NonNull
        public abstract AddProducerRequest build();
    }
}