package com.estasvegano.android.estasvegano.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Category;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.ArrayList;

@FragmentWithArgs
public class ChooseCategoryDialogFragment extends DialogFragment {

    @Arg
    @NonNull
    ArrayList<Category> categories;

    @Nullable
    private ChooseCategoryListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    public void setListener(@Nullable ChooseCategoryListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] categoriesArray = new String[categories.size()];
        for (int i = 0; i < categoriesArray.length; i++) {
            categoriesArray[i] = categories.get(i).getTitle();
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.choose_category_dialog_title)
                .setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoriesArray),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (listener != null) {
                                    listener.categorySelected(which);
                                }
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    public interface ChooseCategoryListener {
        void categorySelected(int position);
    }
}
