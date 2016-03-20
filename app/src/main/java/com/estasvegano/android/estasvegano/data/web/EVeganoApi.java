package com.estasvegano.android.estasvegano.data.web;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.request.AddProducerRequest;
import com.estasvegano.android.estasvegano.data.web.request.AddProductRequest;
import com.estasvegano.android.estasvegano.data.web.response.CategoriesResponse;
import com.estasvegano.android.estasvegano.data.web.response.ProducersResponse;
import com.estasvegano.android.estasvegano.entity.Category;
import com.estasvegano.android.estasvegano.entity.Complain;
import com.estasvegano.android.estasvegano.entity.Photo;
import com.estasvegano.android.estasvegano.entity.Producer;
import com.estasvegano.android.estasvegano.entity.Product;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
            @Part("upload\"; filename=\"image.jpg") @NonNull RequestBody photo
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

    @GET(UrlConstants.PRODUCERS)
    rx.Single<ProducersResponse> getProducers(@Query(UrlConstants.PRODUCERS_TITLE_QUERY) @NonNull String title);
}
