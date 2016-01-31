package com.estasvegano.android.estasvegano.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductType {
    @JsonProperty("vegan")
    VEGAN,
    @JsonProperty("vegetarian")
    VEGETARIAN,
    @JsonProperty("lactovegetarian")
    LACTOVEGETARIAN,
    @JsonProperty("fish")
    FISH,
    @JsonProperty("meat")
    MEAT;
}
