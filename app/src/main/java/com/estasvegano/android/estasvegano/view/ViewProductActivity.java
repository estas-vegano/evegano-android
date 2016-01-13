package com.estasvegano.android.estasvegano.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.estasvegano.android.estasvegano.R;

/**
 * Created by rstk on 12/26/15.
 */
public class ViewProductActivity extends AppCompatActivity
{
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_product);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null)
        {
            fragment = new ViewProductFragment();
            fragment.setRetainInstance(true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        super.onAttachFragment(fragment);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
