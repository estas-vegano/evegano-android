package com.estasvegano.android.estasvegano;

import android.support.annotation.NonNull;

import com.estasvegano.android.estasvegano.data.DataModule;
import com.estasvegano.android.estasvegano.model.ModelModule;
import com.estasvegano.android.estasvegano.view.AddProductFragment;
import com.estasvegano.android.estasvegano.view.CodeReaderFragment;
import com.estasvegano.android.estasvegano.view.ViewProductFragment;
import com.estasvegano.android.estasvegano.view.dialog.ComplainProductDialogFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, AppModule.class, ModelModule.class})
public interface AppComponent {

    void inject(@NonNull CodeReaderFragment codeReaderFragment);

    void inject(@NonNull ViewProductFragment viewProductFragment);

    void inject(@NonNull ComplainProductDialogFragment complainProductDialogFragment);

    void inject(@NonNull AddProductFragment addProductFragment);
}
