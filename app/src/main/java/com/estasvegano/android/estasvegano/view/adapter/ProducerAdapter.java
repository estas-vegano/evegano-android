package com.estasvegano.android.estasvegano.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import com.estasvegano.android.estasvegano.entity.Producer;
import com.estasvegano.android.estasvegano.model.ProducerModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ProducerAdapter extends ArrayAdapter<String> implements Filterable {

    @NonNull
    private final ProducerModel producerModel;
    @NonNull
    private final ProgressBar progressBar;
    @NonNull
    private List<Producer> items = new ArrayList<>(0);

    @NonNull
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(@Nullable CharSequence constraint) {
            Timber.i("Filtering for producer: %s", constraint);

            progressBar.post(() -> progressBar.setVisibility(View.VISIBLE));

            List<Producer> producers = null;
            if (!TextUtils.isEmpty(constraint)) {
                producers = producerModel
                        .getProducers(constraint.toString())
                        .toBlocking()
                        .value();
            }
            producers = producers == null ? new ArrayList<>(0) : producers;

            FilterResults filterResults = new FilterResults();
            filterResults.count = producers.size();
            filterResults.values = producers;
            return filterResults;
        }

        @Override
        protected void publishResults(@Nullable CharSequence constraint, @NonNull FilterResults results) {
            Timber.i("Filtered for producer: %s, result: %s", constraint, results.values);

            progressBar.setVisibility(View.INVISIBLE);
            // noinspection unchecked
            List<Producer> values = (List<Producer>) results.values;
            items = values == null ? new ArrayList<>(0) : values;
            notifyDataSetChanged();
        }
    };

    public ProducerAdapter(
            @NonNull Context context,
            @NonNull ProducerModel producerModel,
            @NonNull ProgressBar producerProgressBar
    ) {
        super(context, android.R.layout.simple_dropdown_item_1line, android.R.id.text1);
        this.producerModel = producerModel;
        this.progressBar = producerProgressBar;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position).title();
    }

    @Nullable
    public Producer getProducerByTitleIfExists(@NonNull String title) {
        for (Producer p : items) {
            if (p.title().equals(title)) {
                return p;
            }
        }
        return null;
    }

    @Override
    @NonNull
    public Filter getFilter() {
        return filter;
    }
}
