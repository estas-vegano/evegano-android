package com.estasvegano.android.estasvegano.model

import android.graphics.Bitmap
import android.support.annotation.CheckResult
import com.estasvegano.android.estasvegano.data.web.EVeganoApi
import com.estasvegano.android.estasvegano.data.web.ErrorParser
import com.estasvegano.android.estasvegano.data.web.request.AddProductRequest
import com.estasvegano.android.estasvegano.entity.Complain
import com.estasvegano.android.estasvegano.entity.Photo
import com.estasvegano.android.estasvegano.entity.Product
import com.estasvegano.android.estasvegano.entity.ProductType
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

// TODO: add caching
class ProductModel(private val api: EVeganoApi, private val errorParser: ErrorParser, private val objectMapper: ObjectMapper) {

    @CheckResult
    fun checkCode(code: String, codeType: String): Single<Product> {
        return api.checkCode(code, codeType)
                .flatMap { errorParser.checkIfError(it) }
                .map { objectMapper.convertValue(it, Product::class.java) }
    }

    @CheckResult
    fun addProduct(
            title: String,
            type: ProductType,
            code: String,
            format: String,
            categoryId: Long,
            producerId: Long
    ): Single<Product> {
        return api.addProduct(
                AddProductRequest(
                        title = title,
                        info = type,
                        categoryId = categoryId,
                        producerId = producerId,
                        code = code,
                        codeType = format
                )
        )
                .flatMap { errorParser.checkIfError(it) }
                .map { objectMapper.convertValue(it, Product::class.java) }
    }

    @CheckResult
    fun uploadPhoto(productId: Long, image: Bitmap): Single<Photo> {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imageBytes = stream.toByteArray()
        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes)
        return api.uploadPhoto(productId, requestBody)
                .flatMap { errorParser.checkIfError(it) }
                .map { objectMapper.convertValue(it, Photo::class.java) }
    }

    @CheckResult
    fun complain(productId: Long, complain: Complain): Single<Any> {
        return api.complain(productId, complain)
                .flatMap { errorParser.checkIfError(it) }
    }
}
