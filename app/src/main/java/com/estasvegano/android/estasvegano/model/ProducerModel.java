package com.estasvegano.android.estasvegano.model;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.ErrorParser;
import com.estasvegano.android.estasvegano.data.web.request.AddProducerRequest;
import com.estasvegano.android.estasvegano.entity.Producer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import rx.Observable;

public class ProducerModel {

    @NonNull
    private final EVeganoApi api;

    @NonNull
    private final ErrorParser errorParser;

    @NonNull
    private final ObjectMapper objectMapper;

    public ProducerModel(
            @NonNull EVeganoApi api,
            @NonNull ErrorParser errorParser,
            @NonNull ObjectMapper objectMapper) {
        this.api = api;
        this.errorParser = errorParser;
        this.objectMapper = objectMapper;
    }

    @CheckResult
    @NonNull
    public rx.Single<Producer> addProducer(@NonNull String title) {
        return api.addProducer(AddProducerRequest.builder().title(title).build())
                .flatMap(errorParser::checkIfError)
                .map(data -> objectMapper.convertValue(data, Producer.class));
    }

    @CheckResult
    @NonNull
    public rx.Single<List<Producer>> getProducers(@NonNull String title) {
        //noinspection unchecked
        return api.getProducers(title)
                .flatMap(errorParser::checkIfError)
                .flatMapObservable(Observable::from)
                .map(data -> objectMapper.convertValue(data, Producer.class))
                .toList()
                .toSingle();
    }
}
