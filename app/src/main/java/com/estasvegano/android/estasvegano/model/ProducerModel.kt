package com.estasvegano.android.estasvegano.model

import android.support.annotation.CheckResult
import com.estasvegano.android.estasvegano.data.web.EVeganoApi
import com.estasvegano.android.estasvegano.data.web.ErrorParser
import com.estasvegano.android.estasvegano.data.web.request.AddProducerRequest
import com.estasvegano.android.estasvegano.entity.Producer
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single

class ProducerModel(
        private val api: EVeganoApi,
        private val errorParser: ErrorParser,
        private val objectMapper: ObjectMapper
) {

    @CheckResult
    fun addProducer(title: String): Single<Producer> {
        return api.addProducer(AddProducerRequest(title))
                .flatMap { errorParser.checkIfError(it) }
                .map<Producer> { data -> objectMapper.convertValue<Producer>(data, Producer::class.java) }
    }

    @CheckResult
    fun getProducers(title: String): Single<List<Producer>> {
        return api.getProducers(title)
                .flatMap { errorParser.checkIfError(it) }
                .map { (it as Iterable<Any>).map { objectMapper.convertValue<Producer>(it, Producer::class.java) } }
    }
}
