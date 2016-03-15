package com.estasvegano.android.estasvegano.data.web.request;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AddProducerRequest.Builder.class)
public abstract class AddProducerRequest implements Parcelable {

    public static Builder builder() {
        return new AutoParcel_AddProducerRequest.Builder();
    }

    @JsonProperty("title")
    public abstract String title();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("title")
        public abstract Builder title(@NonNull String title);

        @NonNull
        public abstract AddProducerRequest build();
    }
}