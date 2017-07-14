package com.estasvegano.android.estasvegano.data.web

import com.estasvegano.android.estasvegano.data.web.request.AddProducerRequest
import com.estasvegano.android.estasvegano.data.web.request.AddProductRequest
import com.estasvegano.android.estasvegano.data.web.response.BaseResponse
import com.estasvegano.android.estasvegano.entity.Complain
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface EVeganoApi {

    @GET(UrlConstants.CHECK)
    fun checkCode(@Query("code") code: String, @Query("type") codeType: String): Single<BaseResponse>

    @POST(UrlConstants.ADD)
    fun addProduct(@Body product: AddProductRequest): Single<BaseResponse>

    @Multipart
    @POST(UrlConstants.ADD_IMAGE)
    fun uploadPhoto(
            @Path(UrlConstants.ID_REPLACEMENT) productId: Long,
            @Part("upload\"; filename=\"image.jpg") photo: RequestBody
    ): Single<BaseResponse>

    @POST(UrlConstants.COMPLAIN)
    fun complain(
            @Path(UrlConstants.ID_REPLACEMENT) productId: Long,
            @Body complain: Complain
    ): Single<BaseResponse>

    @GET(UrlConstants.CATEGORIES)
    fun topCategories(): Single<BaseResponse>

    @GET(UrlConstants.SUB_CATEGORIES)
    fun getCategory(@Path(UrlConstants.ID_REPLACEMENT) categoryId: Long): Single<BaseResponse>

    @POST(UrlConstants.ADD_PRODUCER)
    fun addProducer(@Body producer: AddProducerRequest): Single<BaseResponse>

    @GET(UrlConstants.PRODUCERS)
    fun getProducers(@Query(UrlConstants.PRODUCERS_TITLE_QUERY) title: String): Single<BaseResponse>
}
