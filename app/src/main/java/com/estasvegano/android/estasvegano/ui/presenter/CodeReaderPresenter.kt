package com.estasvegano.android.estasvegano.ui.presenter

import com.estasvegano.android.estasvegano.data.web.ApiException
import com.estasvegano.android.estasvegano.data.web.response.ErrorCode
import com.estasvegano.android.estasvegano.model.ProductModel
import com.estasvegano.android.estasvegano.ui.view.CodeReaderView
import com.estasvegano.android.estasvegano.ui.viewmodels.CodeInfo
import com.estasvegano.android.estasvegano.ui.viewmodels.CodeReaderViewModel
import io.reactivex.Scheduler

class CodeReaderPresenter(initialState: CodeReaderViewModel, val productModel: ProductModel, val config: Config)
    : BasePresenter<CodeReaderView, CodeReaderViewModel>(initialState) {

    override fun bindView(view: CodeReaderView) {
        super.bindView(view)

        var lastCode: CodeInfo = CodeInfo("", "")
        subscribeViewState(safeWrap(view.checkProductEvent)
                .doOnNext({ lastCode = it })
                .flatMapSingle { productModel.checkCode(it.code, it.format) }
                .subscribeOn(config.ioScheduler)
                .observeOn(config.uiScheduler)
                .map { CodeReaderViewModel.found(it) }
                .startWith(CodeReaderViewModel.loading())
                .onErrorReturn({
                    if (it is ApiException && it.errorCode == ErrorCode.PRODUCT_NOT_FOUND) {
                        CodeReaderViewModel.notFound(lastCode)
                    } else {
                        CodeReaderViewModel.error(it)
                    }
                }))
    }

    data class Config(val uiScheduler: Scheduler, val ioScheduler: Scheduler)
}
