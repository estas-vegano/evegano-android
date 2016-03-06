package com.estasvegano.android.estasvegano.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Category;
import com.estasvegano.android.estasvegano.entity.Producer;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.entity.ProductType;

public class CodeReaderActivity extends AppCompatActivity implements CodeReaderFragment.OnCodeReadedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_code_readed);

        CodeReaderFragment fragment = (CodeReaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = new CodeReaderFragment();
            fragment.setRetainInstance(true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        fragment.setListener(this);
    }

    @Override
    public void onProductLoaded(@NonNull Product product) {
        startActivity(ViewProductActivity.getStartIntent(this, product));
    }

    @Override
    public void onNoSuchProduct() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_product_dialog_title)
                .setMessage(R.string.no_product_dialog_message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Product product = Product.builder()
                            .category(Category.builder().id(1).title("Category").build())
                            .id(1)
                            .info(ProductType.VEGAN)
                            .title("BlaBlaBla")
                            .photo("http://blog.4shop.com.ua/wp-content/uploads/2013/01/shtrih_code.jpg")
                            .producer(Producer.builder().ethical(true).id(1).title("Producer").build())
                            .build();
                    startActivity(ViewProductActivity.getStartIntent(this, product));
                })
                .setNegativeButton(android.R.string.no, null)
                .create()
                .show();
    }
}
