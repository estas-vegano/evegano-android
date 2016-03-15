package com.estasvegano.android.estasvegano.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.model.ProductModel;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.Manifest.permission.CAMERA;

@RuntimePermissions
public class CodeReaderFragment extends BaseFragment implements ZBarScannerView.ResultHandler {

    @Inject
    ProductModel productModel;

    @Bind(R.id.reader_scanner)
    ZBarScannerView scannerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    private OnCodeReadedListener listener;

    private boolean cameraPermissionDialogShowing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getVeganoApplication().getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        cameraPermissionDialogShowing = false;

        View view = inflater.inflate(R.layout.fragment_code_readed, container, false);
        ButterKnife.bind(this, view);
        scannerView.setAutoFocus(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!cameraPermissionDialogShowing) {
            CodeReaderFragmentPermissionsDispatcher.startCodeScannerWithCheck(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CodeReaderFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(CAMERA)
    void startCodeScanner() {
        Timber.i("Start scanning");
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void handleResult(@NonNull Result rawResult) {
        Timber.i("New scan result: %d %s %s",
                rawResult.getBarcodeFormat().getId(),
                rawResult.getBarcodeFormat().getName(),
                rawResult.getContents()
        );
        checkProduct(rawResult.getContents(), rawResult.getBarcodeFormat().getName());

        new Handler().postDelayed(() -> {
                    if (isAdded()) {
                        Timber.i("Resume scanning");
                        scannerView.resumeCameraPreview(this);
                    }
                },
                3000L);
    }

    private void checkProduct(@NonNull String code, @NonNull String format) {
        showLoadingDialog();
        Subscription subscription = productModel.checkCode(code, format)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> {
                            hideLoadingDialog();
                            Timber.i("Product loaded: %s", product);
                            if (listener != null) {
                                if (product != null) {
                                    listener.onProductLoaded(product);
                                } else {
                                    listener.onNoSuchProduct(code, format);
                                }
                            }
                        },
                        this::onBaseError);
        unsubscribeOnDestroyView(subscription);
    }

    public void setListener(@Nullable OnCodeReadedListener listener) {
        this.listener = listener;
    }


    @OnShowRationale(CAMERA)
    void onShowPermissionRationale(@NonNull PermissionRequest permissionRequest) {
        cameraPermissionDialogShowing = true;
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.permision_camera_rationale_dialog_title)
                .setMessage(R.string.permision_rationale_dialog_message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    permissionRequest.proceed();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    permissionRequest.cancel();
                })
                .setOnDismissListener(dialog -> cameraPermissionDialogShowing = false)
                .create()
                .show();
    }

    @OnPermissionDenied(CAMERA)
    void onPermissionDenied() {
        cameraPermissionDialogShowing = true;
        scannerView.stopCamera();
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.permision_denied_dialog_title)
                .setMessage(R.string.permision_camera_denied_dialog_message)
                .setPositiveButton(R.string.permision_denied_dialog_positive, (dialog, which) -> {
                    goToPermissionSettings();
                })
                .setOnDismissListener(dialog -> cameraPermissionDialogShowing = false)
                .create()
                .show();
    }

    protected void goToPermissionSettings() {
        cameraPermissionDialogShowing = false;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public interface OnCodeReadedListener {
        void onProductLoaded(@NonNull Product product);

        void onNoSuchProduct(@NonNull String code, @NonNull String format);
    }
}