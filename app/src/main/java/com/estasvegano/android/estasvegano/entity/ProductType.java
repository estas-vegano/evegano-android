package com.estasvegano.android.estasvegano.entity;

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
    UNKNOWN
}
