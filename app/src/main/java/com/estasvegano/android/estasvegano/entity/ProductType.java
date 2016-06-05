package com.estasvegano.android.estasvegano.entity;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductType {
    @JsonProperty("vegan")
    VEGAN,
    @JsonProperty("lactovegetarian")
    LACTOVEGETARIAN,
    @JsonProperty("vegetarian")
    VEGETARIAN,
    @JsonProperty("fish")
    FISH,
    @JsonProperty("meat")
    MEAT,
    @JsonProperty("unknown")
    UNKNOWN;

    @NonNull
    public static ProductType[] valuesToShow() {
        return new ProductType[]{VEGAN, LACTOVEGETARIAN, VEGETARIAN, FISH, MEAT};
    }
}
