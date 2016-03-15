package com.estasvegano.android.estasvegano.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.estasvegano.android.estasvegano.EVeganoApplication;
import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Complain;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.model.ProductModel;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.widget.RxTextView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@FragmentWithArgs
public class ComplainProductDialogFragment extends DialogFragment {

    @SuppressWarnings("NullableProblems") // @Inject
    @Inject
    @NonNull
    ProductModel productModel;

    @SuppressWarnings("NullableProblems") // @Arg
    @Arg
    @NonNull
    Product product;

    @SuppressWarnings("NullableProblems") // @onCreateDialog
    @NonNull
    private EditText messageField;

    @Nullable
    private ComplainDialogListener listener;

    public void setListener(@Nullable ComplainDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((EVeganoApplication) getActivity().getApplication()).getComponent().inject(this);
        FragmentArgs.inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View content = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_complain, null);
        messageField = (EditText) content.findViewById(R.id.dialog_complain_message_field);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.complain_product_dialog_title)
                .setView(content)
                .setPositiveButton(R.string.send, (dialog, which) -> {
                    String message = messageField.getText().toString();
                    if (!TextUtils.isEmpty(message)) {
                        sendComplain(message);
                    } else {
                        Timber.i("Not sending empty complain about product: %s", product);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();

        RxTextView.textChanges(messageField)
                .subscribe(
                        text -> ((AlertDialog) getDialog())
                                .getButton(AlertDialog.BUTTON_POSITIVE)
                                .setEnabled(!TextUtils.isEmpty(text))
                );
    }

    private void sendComplain(@NonNull String message) {
        Timber.i("Sending complain \"%s\" about product: %s", message, product);
        productModel
                .complain(product.id(), Complain.builder().message(message).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            if (listener != null) {
                                listener.onComplainSuccess();
                            }
                        },
                        error -> {
                            if (listener != null) {
                                listener.onComplainError(error);
                            }
                        }
                );
    }

    public interface ComplainDialogListener {

        void onComplainSuccess();

        void onComplainError(@NonNull Throwable throwable);
    }
}
