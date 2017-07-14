package com.estasvegano.android.estasvegano.entity

import com.fasterxml.jackson.annotation.JsonProperty

enum class ProductType(val id: Int) {

    @JsonProperty("vegan")
    VEGAN(0),
    @JsonProperty("lactovegetarian")
    LACTOVEGETARIAN(1),
    @JsonProperty("vegetarian")
    VEGETARIAN(2),
    @JsonProperty("fish")
    FISH(3),
    @JsonProperty("meat")
    MEAT(4),
    @JsonProperty("unknown")
    UNKNOWN(5);


    companion object {

        fun fromId(id: Int): ProductType {
            for (type in ProductType.values()) {
                if (type.id == id) {
                    return type
                }
            }

            throw IllegalAccessException("Unknown id: $id")
        }

        fun valuesToShow(): Array<ProductType> {
            return arrayOf(VEGAN, LACTOVEGETARIAN, VEGETARIAN, FISH, MEAT)
        }
    }
}
