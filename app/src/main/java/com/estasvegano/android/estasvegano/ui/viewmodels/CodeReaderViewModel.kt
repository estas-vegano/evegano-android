package com.estasvegano.android.estasvegano.ui.viewmodels

import com.estasvegano.android.estasvegano.entity.Product

data class CodeReaderViewModel(
        val checkInProgress: Boolean,
        val product: Product? = null,
        val notFoundInfo: CodeInfo? = null,
        val error: Throwable? = null
) : ViewModel {

    companion object {
        fun empty() = CodeReaderViewModel(false)
        fun loading() = CodeReaderViewModel(true)
        fun found(product: Product) = CodeReaderViewModel(false, product = product)
        fun notFound(info: CodeInfo) = CodeReaderViewModel(false, notFoundInfo = info)
        fun error(error: Throwable) = CodeReaderViewModel(false, error = error)
    }
}

data class CodeInfo(val code: String, val format: String)