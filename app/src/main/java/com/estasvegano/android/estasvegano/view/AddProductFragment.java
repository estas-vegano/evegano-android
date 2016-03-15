package com.estasvegano.android.estasvegano.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Category;
import com.estasvegano.android.estasvegano.model.CategoryModel;
import com.estasvegano.android.estasvegano.model.ProducerModel;
import com.estasvegano.android.estasvegano.model.ProductModel;
import com.estasvegano.android.estasvegano.view.adapter.ProducerAdapter;
import com.estasvegano.android.estasvegano.view.adapter.ProductTypeArrayAdapter;
import com.estasvegano.android.estasvegano.view.dialog.ChooseCategoryDialogFragment;
import com.estasvegano.android.estasvegano.view.dialog.ChooseCategoryDialogFragmentBuilder;
import com.estasvegano.android.estasvegano.view.util.ImagePickerHelper;
import com.estasvegano.android.estasvegano.view.util.PicassoCircleBorderTransform;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

@FragmentWithArgs
@RuntimePermissions
public class AddProductFragment extends BaseFragment {

    public static final int MAX_SIZE = 1024;
    public static final int CAMERA_REQUEST_CODE = 161;
    public static final int GALLERY_REQUEST_CODE = 666;

    public static final String CHOOSE_CATEGORY_DIALOG_FRAGMENT_KEY = "CHOOSE_CATEGORY_DIALOG_FRAGMENT";

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
    @Bind(R.id.f_add_product_category_button)
    Button categoryButton;
    @Bind(R.id.f_add_product_image_label)
    TextView productImageLabel;
    @NonNull
    private ImagePickerHelper imagePickerHelper
            = new ImagePickerHelper(MAX_SIZE, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE);
    @Nullable
    private Bitmap productBitmap;
    @Nullable
    private Category selectedCategory;

    public AddProductFragment() {
        setRetainInstance(true);
    }

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

        // restore instance
        if (selectedCategory != null) {
            setSelectedCategory(selectedCategory);
        }
        if (productBitmap != null) {
            showProductImage(productBitmap);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddProductFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick(R.id.f_add_product_image)
    void onImageClicked() {
        imagePickerHelper.showImagePickerDialog(getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_REQUEST_CODE)) {
            AddProductFragmentPermissionsDispatcher.getAvatarBitmapWithCheck(this, requestCode, data);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(READ_EXTERNAL_STORAGE)
    void getAvatarBitmap(int requestCode, Intent data) {
        productBitmap = imagePickerHelper.handleActivityResult(getActivity(), requestCode, data);
        if (productBitmap != null) {
            showProductImage(productBitmap);
        }
    }

    private void showProductImage(@NonNull Bitmap productBitmap) {
        int borderColor = ContextCompat.getColor(getActivity(), R.color.accent);
        int borderWidth = getResources().getDimensionPixelSize(R.dimen.border_width);

        Bitmap copy = productBitmap.copy(productBitmap.getConfig(), false);
        Bitmap roundedImage = new PicassoCircleBorderTransform(borderColor, borderWidth).transform(copy);
        productImage.setImageBitmap(roundedImage);

        productImageLabel.setVisibility(View.GONE);
    }

    private void setSelectedCategory(@NonNull Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        categoryButton.setText(selectedCategory.title());
    }

    @OnClick(R.id.f_add_product_category_button)
    public void onCategoryClicked() {
        if (getFragmentManager().findFragmentByTag(CHOOSE_CATEGORY_DIALOG_FRAGMENT_KEY) != null) {
            return;
        }

        showLoadingDialog();
        Subscription subscription = categoryModel.getTopCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ArrayList<Category>::new)
                .subscribe(
                        categories -> {
                            Timber.i("Categories loaded: %s", categories);
                            hideLoadingDialog();
                            showCategoriesDialog(categories);
                        },
                        error -> onBaseError(error)
                );
        unsubscribeOnDestroyView(subscription);
    }

    private void showCategoriesDialog(@NonNull ArrayList<Category> categories) {
        ChooseCategoryDialogFragment complainProductDialogFragment =
                new ChooseCategoryDialogFragmentBuilder(categories).build();
        complainProductDialogFragment.show(getFragmentManager(), CHOOSE_CATEGORY_DIALOG_FRAGMENT_KEY);
        complainProductDialogFragment.setListener(
                position -> {
                    Category selectedCategory = categories.get(position);
                    if (selectedCategory.isLowLevel()) {
                        showDialogWithChildCategoriesOrReturn(selectedCategory);
                    } else {
                        //noinspection ConstantConditions checked above
                        showCategoriesDialog(new ArrayList<>(selectedCategory.subCategories()));
                    }
                }
        );
    }

    private void showDialogWithChildCategoriesOrReturn(@NonNull Category category) {
        showLoadingDialog();
        Subscription subscription = categoryModel.getCategory(category.id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            hideLoadingDialog();
                            Timber.i("Category loaded: %s", result);
                            if (result.isLowLevel()) {
                                setSelectedCategory(category);
                            } else {
                                //noinspection ConstantConditions checked above
                                showCategoriesDialog(new ArrayList<>(result.subCategories()));
                            }
                        },
                        error -> onBaseError(error)
                );
        unsubscribeOnDestroyView(subscription);
    }


    @OnShowRationale(READ_EXTERNAL_STORAGE)
    void onShowPermissionRationale(@NonNull PermissionRequest permissionRequest) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.permision_storage_rationale_dialog_title)
                .setMessage(R.string.permision_rationale_dialog_message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    permissionRequest.proceed();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    permissionRequest.cancel();
                })
                .create()
                .show();
    }

    @OnPermissionDenied(READ_EXTERNAL_STORAGE)
    void onPermissionDenied() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.permision_denied_dialog_title)
                .setMessage(R.string.permision_storage_denied_dialog_message)
                .setPositiveButton(R.string.permision_denied_dialog_positive, (dialog, which) -> {
                    goToPermissionSettings();
                })
                .create()
                .show();
    }

    protected void goToPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}