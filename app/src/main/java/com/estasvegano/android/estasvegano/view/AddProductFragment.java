package com.estasvegano.android.estasvegano.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.model.CategoryModel;
import com.estasvegano.android.estasvegano.model.ProducerModel;
import com.estasvegano.android.estasvegano.model.ProductModel;
import com.estasvegano.android.estasvegano.view.adapter.ProducerAdapter;
import com.estasvegano.android.estasvegano.view.adapter.ProductTypeArrayAdapter;
import com.estasvegano.android.estasvegano.view.util.ImagePickerHelper;
import com.estasvegano.android.estasvegano.view.util.PicassoCircleBorderTransform;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@FragmentWithArgs
public class AddProductFragment extends BaseFragment {

    public static final int MAX_SIZE = 1024;
    public static final int CAMERA_REQUEST_CODE = 161;
    public static final int GALLERY_REQUEST_CODE = 666;

    @SuppressWarnings("NullableProblems") // onCreateView
    @Arg
    @NonNull
    String code;

    @SuppressWarnings("NullableProblems") // onCreateView
    @Arg
    @NonNull
    String format;

    @SuppressWarnings("NullableProblems") // onCreateView
    @Inject
    @NonNull
    CategoryModel categoryModel;

    @SuppressWarnings("NullableProblems") // onCreateView
    @Inject
    @NonNull
    ProductModel productModel;

    @SuppressWarnings("NullableProblems") // onCreateView
    @Inject
    @NonNull
    ProducerModel producerModel;

    @SuppressWarnings("NullableProblems") // onCreateView
    @Inject
    @NonNull
    Picasso picasso;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.f_add_product_image)
    ImageView productImage;

    @Bind(R.id.f_add_product_title)
    TextView productTitleLabel;

    @Bind(R.id.f_add_product_producer)
    AutoCompleteTextView producerAutoCompleteView;

    @Bind(R.id.f_add_product_type_spinner)
    Spinner productTypeSpinner;

    @Bind(R.id.f_add_product_producer_progress)
    ProgressBar producerProgressBar;

    @NonNull
    private ImagePickerHelper imagePickerHelper
            = new ImagePickerHelper(MAX_SIZE, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE);

    @Nullable
    private Bitmap productBitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        getVeganoApplication().getComponent().inject(this);
        ButterKnife.bind(this, view);
        FragmentArgs.inject(this);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        //noinspection ConstantConditions can not be null here
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productTypeSpinner.setAdapter(new ProductTypeArrayAdapter(getActivity()));

        ProducerAdapter producerAdapter = new ProducerAdapter(getActivity(), producerModel, producerProgressBar);
        producerAutoCompleteView.setAdapter(producerAdapter);
        producerAutoCompleteView.setThreshold(0);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.f_add_product_image)
    void onImageClicked() {
        imagePickerHelper.showImagePickerDialog(getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_REQUEST_CODE)) {
            productBitmap = imagePickerHelper.handleActivityResult(getActivity(), requestCode, data);
            if (productBitmap != null) {
                showProductImage(productBitmap);
            }
        }
    }

    private void showProductImage(@NonNull Bitmap productBitmap) {
        int borderColor = ContextCompat.getColor(getActivity(), R.color.accent);
        int borderWidth = getResources().getDimensionPixelSize(R.dimen.border_width);

        Bitmap copy = productBitmap.copy(productBitmap.getConfig(), false);
        Bitmap roundedImage = new PicassoCircleBorderTransform(borderColor, borderWidth).transform(copy);
        productImage.setImageBitmap(roundedImage);
    }
}
