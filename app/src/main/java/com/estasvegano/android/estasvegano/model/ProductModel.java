package com.estasvegano.android.estasvegano.model;

import android.graphics.Bitmap;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.entity.Category;
import com.estasvegano.android.estasvegano.entity.Complain;
import com.estasvegano.android.estasvegano.entity.Photo;
import com.estasvegano.android.estasvegano.entity.Product;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.ByteArrayOutputStream;
import java.util.Map;

// TODO: add caching
public class ProductModel {

    @NonNull
    private final EVeganoApi api;

    public ProductModel(@NonNull EVeganoApi api) {
        this.api = api;
    }

    @CheckResult
    @NonNull
    public rx.Observable<Product> checkCode(String code, String codeType) {
        return api.checkCode(code, codeType);
    }

    @CheckResult
    @NonNull
    public rx.Observable<Product> addProduct(Product product) {
        return api.addProduct(product);
    }

    @CheckResult
    @NonNull
    public rx.Observable<Photo> uploadPhoto(long productId, Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/json"), imageBytes);
        return api.uploadPhoto(productId, requestBody);
    }

    @CheckResult
    @NonNull
    public rx.Observable<Void> complain(long productId, Complain complain) {
        return api.complain(productId, complain);
    }

    @CheckResult
    @NonNull
    public rx.Observable<Map<String, String>> getTopCategories() {
        return api.getTopCategories();
    }

    @CheckResult
    @NonNull
    public rx.Observable<Category> getSubCategory(long categoryId) {
        return api.getSubCategory(categoryId);
    }
}
