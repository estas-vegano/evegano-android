package com.estasvegano.android.estasvegano.ui.view

interface View<in VM> {

    fun bind(viewModel: VM)
}