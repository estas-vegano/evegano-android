package com.estasvegano.android.estasvegano;

import com.estasvegano.android.estasvegano.data.DataModule;
import com.estasvegano.android.estasvegano.model.ModelModule;
import com.estasvegano.android.estasvegano.view.CodeReaderFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, AppModule.class, ModelModule.class})
public interface AppComponent {

    void inject(CodeReaderFragment codeReaderFragment);
}
