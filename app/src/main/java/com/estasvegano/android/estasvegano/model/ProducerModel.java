package com.estasvegano.android.estasvegano.model;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.ErrorParser;
import com.estasvegano.android.estasvegano.data.web.EVeganoApi;
import com.estasvegano.android.estasvegano.data.web.request.AddProducerRequest;
import com.estasvegano.android.estasvegano.entity.Producer;

import java.util.List;

public class ProducerModel {

    @NonNull
    private final EVeganoApi api;

    @NonNull
    private final ErrorParser errorParser;

    public ProducerModel(@NonNull EVeganoApi api, @NonNull ErrorParser errorParser) {
        this.api = api;
        this.errorParser = errorParser;
    }

    public rx.Single<Void> addProducer(@NonNull String title) {
        return api.addProducer(AddProducerRequest.builder().title(title).build());
    }

    public rx.Single<List<Producer>> getProducers(@NonNull String title) {
        return api.getProducers(title);
    }
}
