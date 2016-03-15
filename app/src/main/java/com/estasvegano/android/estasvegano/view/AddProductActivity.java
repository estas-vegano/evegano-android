package com.estasvegano.android.estasvegano.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.estasvegano.android.estasvegano.R;

public class AddProductActivity extends AppCompatActivity {

    public static final String CODE_EXTRA_KEY = "CODE_EXTRA";
    public static final String FORMAT_EXTRA_KEY = "FORMAT_EXTRA";

    public static Intent getStartIntent(@NonNull Context context, @NonNull String code, @NonNull String format) {
        Intent intent = new Intent(context, AddProductActivity.class);
        intent.putExtra(CODE_EXTRA_KEY, code);
        intent.putExtra(FORMAT_EXTRA_KEY, format);
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

        String code = getIntent().getStringExtra(CODE_EXTRA_KEY);
        String format = getIntent().getStringExtra(FORMAT_EXTRA_KEY);

        setContentView(R.layout.activity_view_product);

        AddProductFragment fragment = (AddProductFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new AddProductFragmentBuilder(code, format).build();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
