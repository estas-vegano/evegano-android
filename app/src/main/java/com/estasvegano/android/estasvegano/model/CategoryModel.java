package com.estasvegano.android.estasvegano.model;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.ErrorParser;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.entity.Category;

import java.util.Map;

public class CategoryModel {

    @NonNull
    private final EVeganoApi api;

    @NonNull
    private final ErrorParser errorParser;

    public CategoryModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser) {
        this.api = api;
        this.errorParser = errorParser;
    }

    @CheckResult
    @NonNull
    public rx.Single<Map<String, String>> getTopCategories() {
        return api.getTopCategories();
    }

    @CheckResult
    @NonNull
    public rx.Single<Category> getSubCategory(long categoryId) {
        return api.getSubCategory(categoryId);
    }
}
