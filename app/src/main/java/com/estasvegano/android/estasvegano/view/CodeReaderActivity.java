package com.estasvegano.android.estasvegano.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.estasvegano.android.estasvegano.R;

import me.dm7.barcodescanner.zbar.BarcodeFormat;

public class CodeReaderActivity extends AppCompatActivity implements CodeReaderFragment.OnCodeReadedListener
{
    public static final String SCAN_RESULT_KEY = "SCAN_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_readed);

        CodeReaderFragment fragment = (CodeReaderFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);

        if (fragment == null)
        {
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
    public void onCodeReaded(String result, BarcodeFormat format)
    {
        startActivity(new Intent(this, ViewProductActivity.class));
    }
}
