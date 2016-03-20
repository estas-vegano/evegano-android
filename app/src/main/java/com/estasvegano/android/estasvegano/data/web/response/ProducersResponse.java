package com.estasvegano.android.estasvegano.data.web.response;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.entity.Producer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import auto.parcel.AutoParcel;

@AutoParcel
@JsonDeserialize(builder = AutoParcel_ProducersResponse.Builder.class)
public abstract class ProducersResponse implements Parcelable {

    @NonNull
    public static Builder builder() {
        return new AutoParcel_ProducersResponse.Builder();
    }

    @JsonProperty("producers")
    @NonNull
    public abstract List<Producer> producers();

    @AutoParcel.Builder
    public static abstract class Builder {

        @JsonProperty("producers")
        @NonNull
        public abstract Builder producers(@NonNull List<Producer> producers);

        @NonNull
        public abstract ProducersResponse build();
    }
}
