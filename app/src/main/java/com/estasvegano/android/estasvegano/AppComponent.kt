package com.estasvegano.android.estasvegano

import com.estasvegano.android.estasvegano.data.DataModule
import com.estasvegano.android.estasvegano.model.ModelModule
import com.estasvegano.android.estasvegano.ui.AddProductFragment
import com.estasvegano.android.estasvegano.ui.CodeReaderFragment
import com.estasvegano.android.estasvegano.ui.ViewProductFragment
import com.estasvegano.android.estasvegano.ui.dialog.ComplainProductDialogFragment
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
