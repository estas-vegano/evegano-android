package com.estasvegano.android.estasvegano.model

import android.support.annotation.CheckResult
import com.estasvegano.android.estasvegano.data.web.EVeganoApi
import com.estasvegano.android.estasvegano.data.web.ErrorParser
import com.estasvegano.android.estasvegano.entity.Category
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single

class CategoryModel(private val api: EVeganoApi, private val errorParser: ErrorParser, private val objectMapper: ObjectMapper) {

    @CheckResult
    fun topCategories(): Single<List<Category>> =
            api.topCategories()
                    .flatMap({ errorParser.checkIfError(it) })
                    .map({ (it as Iterable<Any>).map { parseCategory(it) } })

    @CheckResult
    fun getCategory(categoryId: Long): Single<Category> {
        return api.getCategory(categoryId)
                .flatMap { errorParser.checkIfError(it) }
                .map { parseCategory(it) }
    }

    private fun parseCategory(data: Any): Category {
        return objectMapper.convertValue<Category>(data, Category::class.java)
    }
}
