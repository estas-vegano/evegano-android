package com.estasvegano.android.estasvegano.model;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.ErrorParser;
import com.estasvegano.android.estasvegano.entity.Category;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class CategoryModel {

    @NonNull
    private final EVeganoApi api;

    @NonNull
    private final ErrorParser errorParser;

    @NonNull
    private final ObjectMapper objectMapper;

    public CategoryModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser, @NonNull ObjectMapper objectMapper) {
        this.api = api;
        this.errorParser = errorParser;
        this.objectMapper = objectMapper;
    }

    @CheckResult
    @NonNull
    public rx.Single<List<Category>> getTopCategories() {
        //noinspection unchecked
        return api.getTopCategories()
                .flatMap(errorParser::checkIfError)
                .flatMapObservable(Observable::from)
                .map(this::parseCategory)
                .toList()
                .toSingle();
    }

    @CheckResult
    @NonNull
    public rx.Single<Category> getCategory(long categoryId) {
        return api.getCategory(categoryId)
                .flatMap(errorParser::checkIfError)
                .map(this::parseCategory);
    }

    @NonNull
    private Category parseCategory(@NonNull Object data) {
            Category category = objectMapper.convertValue(data, Category.class);
            List objCategories = category.subCategories();
            if (objCategories != null) {
                ArrayList<Category> parsedCategories = new ArrayList<>(objCategories.size());
                for (Object catObject : objCategories) {
                    parsedCategories.add(objectMapper.convertValue(catObject, Category.class));
                }
                category = category.toBuilder().subCategories(parsedCategories).build();
            }
            return category;
    }
}
