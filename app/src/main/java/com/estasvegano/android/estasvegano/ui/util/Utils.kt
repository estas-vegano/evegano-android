package com.estasvegano.android.estasvegano.ui.util

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

import com.estasvegano.android.estasvegano.R
import com.estasvegano.android.estasvegano.entity.ProductType

object Utils {

    @DrawableRes
    fun getTypeImage(productType: ProductType): Int {
        when (productType) {
            ProductType.VEGAN -> return R.drawable.type_vegan
            ProductType.LACTOVEGETARIAN -> return R.drawable.type_milk
            ProductType.VEGETARIAN -> return R.drawable.type_egg
            ProductType.FISH -> return R.drawable.type_fish
            ProductType.MEAT -> return R.drawable.type_meat
            ProductType.UNKNOWN -> return R.drawable.ethical_unknown
        }
    }

    @DrawableRes
    fun getEthicalImage(isEthical: Boolean?): Int {
        when {
            isEthical == null -> return R.drawable.ethical_unknown
            isEthical -> return R.drawable.ethical_true
            else -> return R.drawable.ethical_false
        }
    }

    @StringRes
    fun getTypeTitle(productType: ProductType): Int {
        when (productType) {
            ProductType.VEGAN -> return R.string.type_vegan
            ProductType.LACTOVEGETARIAN -> return R.string.type_milk
            ProductType.VEGETARIAN -> return R.string.type_egg
            ProductType.FISH -> return R.string.type_fish
            ProductType.MEAT -> return R.string.type_meat
            else -> throw IllegalArgumentException("Invalid product type: " + productType)
        }
    }
}
