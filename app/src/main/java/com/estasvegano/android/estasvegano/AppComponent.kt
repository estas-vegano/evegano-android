package com.estasvegano.android.estasvegano

import com.estasvegano.android.estasvegano.data.DataModule
import com.estasvegano.android.estasvegano.model.ModelModule
import com.estasvegano.android.estasvegano.ui.AddProductFragment
import com.estasvegano.android.estasvegano.ui.CodeReaderFragment.CodeReaderComponent
import com.estasvegano.android.estasvegano.ui.CodeReaderFragment.CodeReaderModule
import com.estasvegano.android.estasvegano.ui.ViewProductFragment
import com.estasvegano.android.estasvegano.ui.dialog.ComplainProductDialogFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class, AppModule::class, ModelModule::class))
interface AppComponent {

    fun inject(viewProductFragment: ViewProductFragment)

    fun inject(complainProductDialogFragment: ComplainProductDialogFragment)

    fun inject(addProductFragment: AddProductFragment)

    fun plus(codeReaderModule: CodeReaderModule): CodeReaderComponent
}
