package com.estasvegano.android.estasvegano.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.view.ViewProductFragment.ViewProductFragmentListener;
import com.estasvegano.android.estasvegano.view.dialog.ComplainProductDialogFragment;
import com.estasvegano.android.estasvegano.view.dialog.ComplainProductDialogFragment.ComplainDialogListener;
import com.estasvegano.android.estasvegano.view.dialog.ComplainProductDialogFragmentBuilder;

import rx.exceptions.CompositeException;

public class ViewProductActivity extends AppCompatActivity
        implements ComplainDialogListener, ViewProductFragmentListener {

    public static final String COMPLAIN_DIALOG_FRAGMENT_KEY = "COMPLAIN_DIALOG_FRAGMENT";

    public static final String PRODUCT_EXTRA_KEY = "PRODUCT_EXTRA";

    public static Intent getStartIntent(@NonNull Context context, @NonNull Product product) {
        Intent intent = new Intent(context, ViewProductActivity.class);
        intent.putExtra(PRODUCT_EXTRA_KEY, product);
        return intent;
    }

    @Nullable
    private ProgressDialog loadingDialog;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Product product = getIntent().getParcelableExtra(PRODUCT_EXTRA_KEY);

        setContentView(R.layout.activity_view_product);

        ViewProductFragment fragment = (ViewProductFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new ViewProductFragmentBuilder(product).build();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        fragment.setListener(this);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ComplainProductDialogFragment) {
            ((ComplainProductDialogFragment) fragment).setListener(this);
        }
    }

    @Override
    public void onComplainClicked(@NonNull Product product) {
        if (getFragmentManager().findFragmentByTag(COMPLAIN_DIALOG_FRAGMENT_KEY) != null) {
            return;
        }

        ComplainProductDialogFragment complainProductDialogFragment =
                new ComplainProductDialogFragmentBuilder(product).build();
        complainProductDialogFragment.show(getSupportFragmentManager(), COMPLAIN_DIALOG_FRAGMENT_KEY);
    }

    @Override
    public void onComplainStarted() {
        showLoadingDialog();
    }

    @Override
    public void onComplainSuccess() {
        hideLoadingDialog();

        new AlertDialog.Builder(this)
                .setTitle(R.string.succes_dialog_title)
                .setMessage(R.string.complain_succes_dialog_message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    @Override
    public void onComplainError(@NonNull Throwable throwable) {
        hideLoadingDialog();

        String message;
        if (throwable instanceof CompositeException) {
            message = ((CompositeException) throwable).getExceptions().get(0).getLocalizedMessage();
        } else {
            message = throwable.getLocalizedMessage();
        }
        new AlertDialog.Builder(this)
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
        loadingDialog = new ProgressDialog(this);
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
