package com.estasvegano.android.estasvegano.data.web;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.request.AddProducerRequest;
import com.estasvegano.android.estasvegano.data.web.request.AddProductRequest;
import com.estasvegano.android.estasvegano.data.web.response.BaseResponse;
import com.estasvegano.android.estasvegano.entity.Complain;

import java.util.LinkedHashMap;
import java.util.List;

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
    rx.Single<BaseResponse<LinkedHashMap>> checkCode(
            @Query("code") @NonNull String code,
            @Query("type") @NonNull String codeType
    );

    @POST(UrlConstants.ADD)
    rx.Single<BaseResponse<LinkedHashMap>> addProduct(@Body @NonNull AddProductRequest product);

    @Multipart
    @POST(UrlConstants.ADD_IMAGE)
    rx.Single<BaseResponse<LinkedHashMap>> uploadPhoto(
            @Path(UrlConstants.ID_REPLACEMENT) long productId,
            @Part("upload\"; filename=\"image.jpg") @NonNull RequestBody photo
    );

    @POST(UrlConstants.COMPLAIN)
    rx.Single<BaseResponse<Void>> complain(
            @Path(UrlConstants.ID_REPLACEMENT) long productId,
            @Body @NonNull Complain complain
    );

    @GET(UrlConstants.CATEGORIES)
    rx.Single<BaseResponse<List>> getTopCategories();

    @GET(UrlConstants.SUB_CATEGORIES)
    rx.Single<BaseResponse<LinkedHashMap>> getCategory(
            @Path(UrlConstants.ID_REPLACEMENT) long categoryId
    );

    @POST(UrlConstants.ADD_PRODUCER)
    rx.Single<BaseResponse<LinkedHashMap>> addProducer(@Body @NonNull AddProducerRequest producer);

    @GET(UrlConstants.PRODUCERS)
    rx.Single<BaseResponse<List>> getProducers(
            @Query(UrlConstants.PRODUCERS_TITLE_QUERY) @NonNull String title
    );
}
