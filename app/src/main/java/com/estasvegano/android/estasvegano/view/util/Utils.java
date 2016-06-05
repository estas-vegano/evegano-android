package com.estasvegano.android.estasvegano.view.util;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.ProductType;

public class Utils {

    @DrawableRes
    public static int getTypeImage(@NonNull ProductType productType) {
        switch (productType) {
            case VEGAN:
                return R.drawable.type_vegan;
            case LACTOVEGETARIAN:
                return R.drawable.type_milk;
            case VEGETARIAN:
                return R.drawable.type_egg;
            case FISH:
                return R.drawable.type_fish;
            case MEAT:
                return R.drawable.type_meat;
            case UNKNOWN:
                return R.drawable.ethical_unknown;
            default:
                throw new IllegalArgumentException("Invalid product type: " + productType);
        }
    }

    @DrawableRes
    public static int getEthicalImage(@Nullable Boolean isEthical) {
        if (isEthical == null) {
            return R.drawable.ethical_unknown;
        } else if (isEthical) {
            return R.drawable.ethical_true;
        } else {
            return R.drawable.ethical_false;
        }
    }

    @StringRes
    public static int getTypeTitle(@NonNull ProductType productType) {
        switch (productType) {
            case VEGAN:
                return R.string.type_vegan;
            case LACTOVEGETARIAN:
                return R.string.type_milk;
            case VEGETARIAN:
                return R.string.type_egg;
            case FISH:
                return R.string.type_fish;
            case MEAT:
                return R.string.type_meat;
            default:
                throw new IllegalArgumentException("Invalid product type: " + productType);
        }
    }
}
