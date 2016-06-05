package com.estasvegano.android.estasvegano.model;

import android.graphics.Bitmap;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.ErrorParser;
import com.estasvegano.android.estasvegano.data.web.request.AddProductRequest;
import com.estasvegano.android.estasvegano.entity.Complain;
import com.estasvegano.android.estasvegano.entity.Photo;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.entity.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;

// TODO: add caching
public class ProductModel {

    @NonNull
    private final EVeganoApi api;

    @NonNull
    private final ErrorParser errorParser;

    @NonNull
    private final ObjectMapper objectMapper;

    public ProductModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser, @NonNull ObjectMapper objectMapper) {
        this.api = api;
        this.errorParser = errorParser;
        this.objectMapper = objectMapper;
    }

    @CheckResult
    @NonNull
    public rx.Single<Product> checkCode(String code, String codeType) {
        return api.checkCode(code, codeType)
                .flatMap(errorParser::checkIfError)
                .map(data -> objectMapper.convertValue(data, Product.class));
    }

    @CheckResult
    @NonNull
    public rx.Single<Product> addProduct(
            @NonNull String title,
            @NonNull ProductType type,
            @NonNull String code,
            @NonNull String format,
            long categoryId,
            long producerId
    ) {
        return api.addProduct(
                AddProductRequest.builder()
                        .title(title)
                        .info(type)
                        .categoryId(categoryId)
                        .producerId(producerId)
                        .code(code)
                        .codeType(format)
                        .build()
        )
                .flatMap(errorParser::checkIfError)
                .map(data -> objectMapper.convertValue(data, Product.class));
    }

    @CheckResult
    @NonNull
    public rx.Single<Photo> uploadPhoto(long productId, Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        return api.uploadPhoto(productId, requestBody)
                .flatMap(errorParser::checkIfError)
                .map(data -> objectMapper.convertValue(data, Photo.class));
    }

    @CheckResult
    @NonNull
    public rx.Single<Void> complain(long productId, Complain complain) {
        return api.complain(productId, complain).flatMap(errorParser::checkIfError);
    }
}
