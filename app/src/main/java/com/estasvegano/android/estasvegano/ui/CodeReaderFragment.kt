package com.estasvegano.android.estasvegano.ui

import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.estasvegano.android.estasvegano.R
import com.estasvegano.android.estasvegano.entity.Product
import com.estasvegano.android.estasvegano.model.ProductModel
import com.estasvegano.android.estasvegano.ui.dialog.AddProductDialogFragmentBuilder
import com.estasvegano.android.estasvegano.ui.presenter.CodeReaderPresenter
import com.estasvegano.android.estasvegano.ui.util.assertActivityImplementsInterface
import com.estasvegano.android.estasvegano.ui.util.findOrThrow
import com.estasvegano.android.estasvegano.ui.util.lazyView
import com.estasvegano.android.estasvegano.ui.view.CodeReaderView
import com.estasvegano.android.estasvegano.ui.viewmodels.CodeInfo
import com.estasvegano.android.estasvegano.ui.viewmodels.CodeReaderViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import permissions.dispatcher.*
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
class CodeReaderFragment : BaseFragment(), ZBarScannerView.ResultHandler, CodeReaderView {

    internal val scannerView: ZBarScannerView by lazyView(R.id.reader_scanner)

    internal val toolbar: Toolbar by lazyView(R.id.toolbar)

    private var cameraPermissionDialogShowing: Boolean = false

    override val checkProductEvent: PublishSubject<CodeInfo> = PublishSubject.create()

    @set:Inject
    lateinit var presenter: CodeReaderPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        assertActivityImplementsInterface(context, OnCodeReadedListener::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        veganoApplication.component.plus(CodeReaderModule()).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_code_readed, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scannerView.setAutoFocus(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        cameraPermissionDialogShowing = false

        presenter.bindView(this)
    }

    override fun onResume() {
        super.onResume()

        if (!cameraPermissionDialogShowing) {
            CodeReaderFragmentPermissionsDispatcher.startCodeScannerWithCheck(this)
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onDestroy() {
        presenter.unBindView(this)
        super.onDestroy()
    }

    @NeedsPermission(CAMERA)
    fun startCodeScanner() {
        Timber.i("Start scanning")
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun handleResult(rawResult: Result) {
        Timber.i("New scan result: ${rawResult.barcodeFormat.id} ${rawResult.barcodeFormat.name} ${rawResult.contents}")

        Handler().postDelayed({
            if (isAdded) {
                Timber.i("Resume scanning")
                scannerView.resumeCameraPreview(this@CodeReaderFragment)
            }
        },
                3000L)

        checkProductEvent.onNext(CodeInfo(code = rawResult.contents, format = rawResult.barcodeFormat.name))
    }

    override fun bind(viewModel: CodeReaderViewModel) {
        Timber.i("Bind view model $viewModel")
        if (viewModel.checkInProgress) {
            showLoadingDialog()
        } else if (viewModel.product != null) {
            hideLoadingDialog()
            castedActivity<OnCodeReadedListener>().onProductLoaded(viewModel.product)
        } else if (viewModel.notFoundInfo != null) {
            hideLoadingDialog()
            showAddDialog(viewModel.notFoundInfo)
        } else if (viewModel.error != null) {
            onBaseError(viewModel.error)
        } else {
            hideLoadingDialog()
        }
    }

    private fun showAddDialog(code: CodeInfo) {
        Timber.i("No product found")

        AlertDialog.Builder(activity)
                .setTitle(R.string.no_product_dialog_title)
                .setMessage(R.string.no_product_dialog_message)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    castedActivity<OnCodeReadedListener>().onNoSuchProduct(code = code.code, format = code.format)
                }
                .setNegativeButton(android.R.string.no, null)
                .create();
    }

    //region permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CodeReaderFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }

    @OnShowRationale(CAMERA)
    fun onShowPermissionRationale(permissionRequest: PermissionRequest) {
        cameraPermissionDialogShowing = true
        AlertDialog.Builder(activity)
                .setTitle(R.string.permision_camera_rationale_dialog_title)
                .setMessage(R.string.permision_rationale_dialog_message)
                .setPositiveButton(android.R.string.ok) { _, _ -> permissionRequest.proceed() }
                .setNegativeButton(android.R.string.cancel) { _, _ -> permissionRequest.cancel() }
                .setOnDismissListener { cameraPermissionDialogShowing = false }
                .create()
                .show()
    }

    @OnPermissionDenied(CAMERA)
    fun onPermissionDenied() {
        cameraPermissionDialogShowing = true
        scannerView.stopCamera()
        AlertDialog.Builder(activity)
                .setTitle(R.string.permision_denied_dialog_title)
                .setMessage(R.string.permision_camera_denied_dialog_message)
                .setPositiveButton(R.string.permision_denied_dialog_positive) { _, _ -> goToPermissionSettings() }
                .setOnDismissListener { cameraPermissionDialogShowing = false }
                .create()
                .show()
    }

    fun goToPermissionSettings() {
        cameraPermissionDialogShowing = false
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        startActivity(intent)
    }
    //endregion

    interface OnCodeReadedListener {

        fun onProductLoaded(product: Product)

        fun onNoSuchProduct(code: String, format: String)
    }

    @Module
    class CodeReaderModule {

        @Provides
        fun provideCodeReaderPresenter(productModel: ProductModel) =
                CodeReaderPresenter(
                        CodeReaderViewModel.empty(),
                        productModel,
                        CodeReaderPresenter.Config(uiScheduler = AndroidSchedulers.mainThread(), ioScheduler = Schedulers.io())
                )
    }

    @Subcomponent(modules = arrayOf(CodeReaderModule::class))
    interface CodeReaderComponent {

        fun inject(fragment: CodeReaderFragment)
    }
}