package com.estasvegano.android.estasvegano.data.web;

import com.estasvegano.android.estasvegano.model.Category;
import com.estasvegano.android.estasvegano.model.Complain;
import com.estasvegano.android.estasvegano.model.Photo;
import com.estasvegano.android.estasvegano.model.Product;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by rstk on 1/13/16.
 */
public interface EVeganoApi
{
    @GET(UrlConstants.CHECK)
    rx.Observable<Product> checkCode(@Query("code") String code, @Query("type") String codeType);

    @POST(UrlConstants.ADD)
    rx.Observable<Product> addProduct(@Body Product product);

    @Multipart
    @POST(UrlConstants.ADD_IMAGE)
    rx.Observable<Photo> uploadPhoto(@Path(UrlConstants.ID_REPLACEMENT) long productId, @Part("image.jpg") RequestBody photo);

    @POST(UrlConstants.COMPLAIN)
    rx.Observable<Void> complain(@Body Complain complain);

    @GET(UrlConstants.CATEGORIES)
    rx.Observable<Map<String, String>> getTopCategories();

    @GET(UrlConstants.SUB_CATEGORIES)
    rx.Observable<Category> getSubCategory(@Path(UrlConstants.ID_REPLACEMENT) long categoryId);
}
