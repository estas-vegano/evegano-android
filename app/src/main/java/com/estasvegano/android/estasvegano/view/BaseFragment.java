package com.estasvegano.android.estasvegano.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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
import timber.log.Timber;

public class BaseFragment extends Fragment {

    @SuppressWarnings("NullableProblems") // onCreate
    @NonNull
    private CompositeSubscription compositeSubcscription;

    @Nullable
    private ProgressDialog loadingDialog;

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
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setTitle(title);
    }

    protected void unsubscribeOnDestroyView(Subscription subscription) {
        compositeSubcscription.add(subscription);
    }

    protected EVeganoApplication getVeganoApplication() {
        return (EVeganoApplication) getActivity().getApplication();
    }

    protected void onBaseError(@NonNull Throwable throwable) {
        Timber.e(throwable, "Error ");

        if (!isAdded()) {
            return;
        }

        hideLoadingDialog();

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

    protected void showLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage(getString(R.string.loading_dialog_message));
        loadingDialog.show();
    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
