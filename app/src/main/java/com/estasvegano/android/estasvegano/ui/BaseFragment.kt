package com.estasvegano.android.estasvegano.ui

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.estasvegano.android.estasvegano.EVeganoApplication
import com.estasvegano.android.estasvegano.R
import com.estasvegano.android.estasvegano.data.web.ApiException
import com.estasvegano.android.estasvegano.ui.util.assertActivityImplementsInterface
import timber.log.Timber

open class BaseFragment : Fragment() {

    private var loadingDialog: ProgressDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        assertActivityImplementsInterface(context, NonConfigurationObjectsStore::class.java)
    }

    protected fun setTitle(title: String) {
        val supportActionBar = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.title = title
    }

    protected val veganoApplication: EVeganoApplication
        get() = activity.application as EVeganoApplication

    protected val nonConfigurationObjectStore: NonConfigurationObjectsStore
        get() = activity as NonConfigurationObjectsStore

    protected inline fun <reified T> castedActivity(): T = activity as T

    @UiThread
    protected fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(activity).apply {
                setMessage(getString(R.string.loading_dialog_message))
                show()
            }
        }
    }

    @UiThread
    protected fun hideLoadingDialog() {
        val dialog = loadingDialog
        if (dialog != null) {
            dialog.dismiss()
            loadingDialog = null
        }
    }

    protected fun onBaseError(throwable: Throwable) {
        Timber.e(throwable, "Error")

        if (!isAdded) {
            return
        }

        hideLoadingDialog()

        val message = if (throwable is ApiException) {
            Html.fromHtml("${getString(R.string.error_dialog_message)} \n<tt> ${throwable.message} </tt>")
        } else {
            getString(R.string.error_dialog_message)
        }
        AlertDialog.Builder(activity)
                .setTitle(R.string.error_dialog_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show()
    }
}
