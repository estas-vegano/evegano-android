package com.estasvegano.android.estasvegano.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = messageField.getText().toString();
                        if (!TextUtils.isEmpty(message)) {
                            ComplainProductDialogFragment.this.sendComplain(message);
                        } else {
                            Timber.i("Not sending empty complain about product: %s", product);
                        }
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
                        new Consumer<CharSequence>() {
                            @Override
                            public void accept(CharSequence text) throws Exception {
                                ((AlertDialog) ComplainProductDialogFragment.this.getDialog())
                                        .getButton(AlertDialog.BUTTON_POSITIVE)
                                        .setEnabled(!TextUtils.isEmpty(text));
                            }
                        }
                );
    }

    private void sendComplain(@NonNull String message) {
        if (listener != null) {
            listener.onComplainStarted();
        }
        Timber.i("Sending complain \"%s\" about product: %s", message, product);
        productModel
                .complain(product.getId(), new Complain(message))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Object>() {
                            @Override
                            public void accept(Object result) throws Exception {
                                if (listener != null) {
                                    listener.onComplainSuccess();
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable error) throws Exception {
                                if (listener != null) {
                                    listener.onComplainError(error);
                                }
                            }
                        }
                );
    }

    public interface ComplainDialogListener {

        void onComplainStarted();

        void onComplainSuccess();

        void onComplainError(@NonNull Throwable throwable);
    }
}
