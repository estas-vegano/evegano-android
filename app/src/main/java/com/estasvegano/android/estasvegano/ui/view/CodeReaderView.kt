package com.estasvegano.android.estasvegano.ui.view

import com.estasvegano.android.estasvegano.ui.viewmodels.CodeInfo
import com.estasvegano.android.estasvegano.ui.viewmodels.CodeReaderViewModel
import io.reactivex.Observable

interface CodeReaderView: View<CodeReaderViewModel> {

    val checkProductEvent: Observable<CodeInfo>
}