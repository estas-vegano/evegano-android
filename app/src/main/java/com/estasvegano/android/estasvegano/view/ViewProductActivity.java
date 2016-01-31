package com.estasvegano.android.estasvegano.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Product;

public class ViewProductActivity extends AppCompatActivity {

    public static final String PRODUCT_EXTRA_KEY = "PRODUCT_EXTRA";

    public static Intent getStartIntent(@NonNull Context context, @NonNull Product product) {
        Intent intent = new Intent(context, ViewProductActivity.class);
        intent.putExtra(PRODUCT_EXTRA_KEY, product);
        return intent;
    }

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

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new ViewProductFragmentBuilder(product).build();
            fragment.setRetainInstance(true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
