package com.estasvegano.android.estasvegano.data.web;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.request.AddProducerRequest;
import com.estasvegano.android.estasvegano.data.web.request.AddProductRequest;
import com.estasvegano.android.estasvegano.data.web.response.CategoriesResponse;
import com.estasvegano.android.estasvegano.entity.Category;
import com.estasvegano.android.estasvegano.entity.Complain;
import com.estasvegano.android.estasvegano.entity.Photo;
import com.estasvegano.android.estasvegano.entity.Producer;
import com.estasvegano.android.estasvegano.entity.Product;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

public interface EVeganoApi {

    @GET(UrlConstants.CHECK)
    rx.Single<Product> checkCode(
            @Query("code") @NonNull String code,
            @Query("type") @NonNull String codeType
    );

    @POST(UrlConstants.ADD)
    rx.Single<Product> addProduct(@Body @NonNull AddProductRequest product);

    @Multipart
    @POST(UrlConstants.ADD_IMAGE)
    rx.Single<Photo> uploadPhoto(
            @Path(UrlConstants.ID_REPLACEMENT) long productId,
            @Part("image.jpg") @NonNull RequestBody photo
    );

    @POST(UrlConstants.COMPLAIN)
    rx.Single<Void> complain(
            @Path(UrlConstants.ID_REPLACEMENT) long productId,
            @Body @NonNull Complain complain
    );

    @GET(UrlConstants.CATEGORIES)
    rx.Single<CategoriesResponse> getTopCategories();

    @GET(UrlConstants.SUB_CATEGORIES)
    rx.Single<Category> getCategory(@Path(UrlConstants.ID_REPLACEMENT) long categoryId);

    @POST(UrlConstants.ADD_PRODUCER)
    rx.Single<Producer> addProducer(@Body @NonNull AddProducerRequest producer);

    @POST(UrlConstants.PRODUCERS)
    rx.Single<List<Producer>> getProducers(@Path(UrlConstants.ID_REPLACEMENT) @NonNull String title);
}
