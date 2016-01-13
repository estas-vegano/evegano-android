package com.estasvegano.android.estasvegano.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estasvegano.android.estasvegano.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rstk on 12/26/15.
 */
public class ViewProductFragment extends Fragment
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_view_product, container, false);
        ButterKnife.bind(this, v);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
