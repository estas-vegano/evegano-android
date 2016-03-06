package com.estasvegano.android.estasvegano.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.canelmas.let.AskPermission;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;
import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.model.ProductModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CodeReaderFragment extends BaseFragment
        implements ZBarScannerView.ResultHandler, RuntimePermissionListener {

    @Inject
    ProductModel productModel;

    @Bind(R.id.reader_scanner)
    ZBarScannerView scannerView;

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
            startCodeScanner();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @AskPermission(Manifest.permission.CAMERA)
    private void startCodeScanner() {
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void handleResult(@NonNull Result rawResult) {
        checkProduct(rawResult.getContents(), rawResult.getBarcodeFormat().getName());
        new Handler().postDelayed(() -> scannerView.resumeCameraPreview(this), 3000L);
    }

    @AskPermission(Manifest.permission.INTERNET)
    private void checkProduct(String code, String format) {
        Subscription subscription = productModel.checkCode(code, format)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::productLoaded, this::onBaseError);
        unsubscriveOnDestroyView(subscription);
    }

    private void productLoaded(@Nullable Product product) {
        if (listener != null) {
            if (product != null) {
                listener.onProductLoaded(product);
            } else {
                listener.onNoSuchProduct();
            }
        }
    }

    public void setListener(@Nullable OnCodeReadedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        Let.handle(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onShowPermissionRationale(
            @NonNull List<String> permissionList,
            @NonNull RuntimePermissionRequest permissionRequest
    ) {
        for (String permission : permissionList) {
            switch (permission) {
                case Manifest.permission.CAMERA:
                    cameraPermissionDialogShowing = true;
                    showCameraPermissionRationaleDialog(permissionRequest);
                    break;
                default:
                    throw new IllegalStateException("Invalid permission " + permission);
            }
        }
    }

    @Override
    public void onPermissionDenied(@NonNull List<DeniedPermission> deniedPermissionList) {
        for (DeniedPermission permission : deniedPermissionList) {
            if (!permission.isNeverAskAgainChecked()) {
                continue;
            }
            switch (permission.getPermission()) {
                case Manifest.permission.CAMERA:
                    cameraPermissionDialogShowing = true;
                    scannerView.stopCamera();
                    showCameraPermissionDeniedDialog();
                    break;
                default:
                    throw new IllegalStateException("Invalid permission " + permission.getPermission());
            }
        }
    }

    private void showCameraPermissionRationaleDialog(@NonNull RuntimePermissionRequest permissionRequest) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.permision_camera_rationale_dialog_title)
                .setMessage(R.string.permision_camera_rationale_dialog_message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    cameraPermissionDialogShowing = false;
                    permissionRequest.retry();
                })
                .setOnDismissListener(dialog -> cameraPermissionDialogShowing = false)
                .create()
                .show();
    }

    private void showCameraPermissionDeniedDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.permision_camera_denied_dialog_title)
                .setMessage(R.string.permision_camera_denied_dialog_message)
                .setPositiveButton(R.string.permision_denied_dialog_positive, (dialog, which) -> {
                    goToPermissionSettings();
                })
                .setOnDismissListener(dialog -> cameraPermissionDialogShowing = false)
                .create()
                .show();
    }

    private void goToPermissionSettings() {
        cameraPermissionDialogShowing = false;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public interface OnCodeReadedListener {
        void onProductLoaded(@NonNull Product product);

        void onNoSuchProduct();
    }
}
