package com.estasvegano.android.estasvegano

import com.estasvegano.android.estasvegano.data.DataModule
import com.estasvegano.android.estasvegano.model.ModelModule
import com.estasvegano.android.estasvegano.view.AddProductFragment
import com.estasvegano.android.estasvegano.view.CodeReaderFragment
import com.estasvegano.android.estasvegano.view.ViewProductFragment
import com.estasvegano.android.estasvegano.view.dialog.ComplainProductDialogFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class, AppModule::class, ModelModule::class))
interface AppComponent {

    fun inject(codeReaderFragment: CodeReaderFragment)

    fun inject(viewProductFragment: ViewProductFragment)

    fun inject(complainProductDialogFragment: ComplainProductDialogFragment)

    fun inject(addProductFragment: AddProductFragment)
}
