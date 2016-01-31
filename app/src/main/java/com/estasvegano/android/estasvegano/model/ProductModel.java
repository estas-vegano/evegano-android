package com.estasvegano.android.estasvegano.model;

import android.graphics.Bitmap;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.ErrorParser;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.entity.Complain;
import com.estasvegano.android.estasvegano.entity.Photo;
import com.estasvegano.android.estasvegano.entity.Product;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.ByteArrayOutputStream;

// TODO: add caching
public class ProductModel {

    @NonNull
    private final EVeganoApi api;

    @NonNull
    private final ErrorParser errorParser;

    public ProductModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser) {
        this.api = api;
        this.errorParser = errorParser;
    }

    @CheckResult
    @NonNull
    public rx.Single<Product> checkCode(String code, String codeType) {
        return api.checkCode(code, codeType)
                .onErrorReturn(error -> {
                    if (errorParser.getErrorStatus(error) == 404) {
                        return null;
                    }
                    throw new IllegalArgumentException(error);
                });
    }

    @CheckResult
    @NonNull
    public rx.Single<Product> addProduct(Product product) {
        return api.addProduct(product);
    }

    @CheckResult
    @NonNull
    public rx.Single<Photo> uploadPhoto(long productId, Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/json"), imageBytes);
        return api.uploadPhoto(productId, requestBody);
    }

    @CheckResult
    @NonNull
    public rx.Single<Void> complain(long productId, Complain complain) {
        return api.complain(productId, complain);
    }
}
