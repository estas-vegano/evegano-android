package com.estasvegano.android.estasvegano.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.view.dialog.AddProductDialogFragmentBuilder;

import timber.log.Timber;

public class CodeReaderActivity extends AppCompatActivity implements CodeReaderFragment.OnCodeReadedListener {

    public static final String ADD_DIALOG_FRAGMENT_KEY = "ADD_DIALOG_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_code_readed);

        CodeReaderFragment fragment = (CodeReaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = new CodeReaderFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        fragment.setListener(this);
    }

    @Override
    public void onProductLoaded(@NonNull Product product) {
        Timber.i("Going to view product activity with product: %s", product);
        startActivity(ViewProductActivity.getStartIntent(this, product));
    }

    @Override
    public void onNoSuchProduct(@NonNull String code, @NonNull String format) {
        Timber.i("No product found");
        DialogFragment addFragment = (DialogFragment) getSupportFragmentManager()
                .findFragmentByTag(ADD_DIALOG_FRAGMENT_KEY);
        if (addFragment != null) {
            return;
        }

        addFragment = new AddProductDialogFragmentBuilder(code, format).build();
        addFragment.show(getSupportFragmentManager(), ADD_DIALOG_FRAGMENT_KEY);
    }
}
