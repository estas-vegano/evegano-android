package com.estasvegano.android.estasvegano.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.view.AddProductActivity;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import timber.log.Timber;

@FragmentWithArgs
public class AddProductDialogFragment extends DialogFragment {

    @Arg
    @NonNull
    String code;

    @Arg
    @NonNull
    String format;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.no_product_dialog_title)
                .setMessage(R.string.no_product_dialog_message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Timber.i("Going to add product activity with code: %s format: %s", code, format);
                    startActivity(AddProductActivity.getStartIntent(getActivity(), code, format));
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
    }
}
