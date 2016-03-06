package com.estasvegano.android.estasvegano.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estasvegano.android.estasvegano.EVeganoApplication;
import com.estasvegano.android.estasvegano.R;

import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.subscriptions.CompositeSubscription;

public class BaseFragment extends Fragment {

    @SuppressWarnings("NullableProblems") // onCreate
    @NonNull
    private CompositeSubscription compositeSubcscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        compositeSubcscription = new CompositeSubscription();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        compositeSubcscription.unsubscribe();
        super.onDestroyView();
    }

    protected void setTitle(@NonNull String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    protected void unsubscriveOnDestroyView(Subscription subscription) {
        compositeSubcscription.add(subscription);
    }

    protected EVeganoApplication getVeganoApplication() {
        return (EVeganoApplication) getActivity().getApplication();
    }

    protected void onBaseError(@NonNull Throwable throwable) {
        String message;
        if (throwable instanceof CompositeException) {
            message = ((CompositeException) throwable).getExceptions().get(0).getLocalizedMessage();
        } else {
            message = throwable.getLocalizedMessage();
        }
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error_dialog_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }
}
