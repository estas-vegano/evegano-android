package com.estasvegano.android.estasvegano.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.estasvegano.android.estasvegano.R
import com.estasvegano.android.estasvegano.entity.Category
import com.estasvegano.android.estasvegano.entity.ProductType
import com.estasvegano.android.estasvegano.model.CategoryModel
import com.estasvegano.android.estasvegano.model.ProducerModel
import com.estasvegano.android.estasvegano.model.ProductModel
import com.estasvegano.android.estasvegano.view.adapter.ProducerAdapter
import com.estasvegano.android.estasvegano.view.adapter.ProductTypeArrayAdapter
import com.estasvegano.android.estasvegano.view.dialog.ChooseCategoryDialogFragmentBuilder
import com.estasvegano.android.estasvegano.view.util.ImagePickerHelper
import com.estasvegano.android.estasvegano.view.util.PicassoCircleBorderTransform
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import permissions.dispatcher.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@FragmentWithArgs
@RuntimePermissions
class AddProductFragment : BaseFragment() {

    // onCreateView
    @Arg
    lateinit internal var code: String

    // onCreateView
    @Arg
    lateinit internal var format: String

    // onCreateView
    @Inject
    lateinit internal var categoryModel: CategoryModel

    // onCreateView
    @Inject
    lateinit internal var productModel: ProductModel

    // onCreateView
    @Inject
    lateinit internal var producerModel: ProducerModel

    // onCreateView
    @Inject
    lateinit internal var picasso: Picasso

    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null

    @BindView(R.id.f_add_product_image)
    internal var productImage: ImageView? = null

    @BindView(R.id.f_add_product_title_field)
    internal var productTitleLabel: TextView? = null

    @BindView(R.id.f_add_product_producer_field)
    internal var producerAutoCompleteView: AutoCompleteTextView? = null

    @BindView(R.id.f_add_product_type_spinner)
    internal var productTypeSpinner: Spinner? = null

    @BindView(R.id.f_add_product_producer_progress)
    internal var producerProgressBar: ProgressBar? = null

    @BindView(R.id.f_add_product_category_button)
    internal var categoryButton: Button? = null

    @BindView(R.id.f_add_product_image_label)
    internal var productImageLabel: TextView? = null

    @BindView(R.id.f_add_product_image_container)
    internal var productImageContainer: View? = null

    private val imagePickerHelper = ImagePickerHelper(MAX_SIZE, CAMERA_REQUEST_CODE, GALLERY_REQUEST_CODE)

    private var productBitmap: Bitmap? = null

    private var selectedCategory: Category? = null

    lateinit private var producerAdapter: ProducerAdapter

    init {
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_add_product, container, false)

        veganoApplication.component.inject(this)
        ButterKnife.bind(this, view)
        FragmentArgs.inject(this)

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)

        appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        productTypeSpinner!!.adapter = ProductTypeArrayAdapter(activity)

        producerAdapter = ProducerAdapter(activity, producerModel, producerProgressBar!!)
        producerAutoCompleteView!!.setAdapter(producerAdapter)
        producerAutoCompleteView!!.threshold = 0

        // restore instance
        if (selectedCategory != null) {
            setSelectedCategory(selectedCategory!!)
        }
        if (productBitmap != null) {
            showProductImage(productBitmap!!)
        }

        return view
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AddProductFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }

    @OnClick(R.id.f_add_product_image)
    internal fun onImageClicked() {
        imagePickerHelper.showImagePickerDialog(activity, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && (requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_REQUEST_CODE)) {
            AddProductFragmentPermissionsDispatcher.`getAvatarBitmap$app_debugWithCheck`(this, requestCode, data)
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @NeedsPermission(READ_EXTERNAL_STORAGE)
    internal fun getAvatarBitmap(requestCode: Int, data: Intent) {
        productBitmap = imagePickerHelper.handleActivityResult(activity, requestCode, data)
        if (productBitmap != null) {
            showProductImage(productBitmap!!)
        }
    }

    private fun showProductImage(productBitmap: Bitmap) {
        val borderColor = ContextCompat.getColor(activity, R.color.accent)
        val borderWidth = resources.getDimensionPixelSize(R.dimen.border_width)

        val copy = productBitmap.copy(productBitmap.config, false)
        val roundedImage = PicassoCircleBorderTransform(borderColor, borderWidth).transform(copy)
        productImage!!.setImageBitmap(roundedImage)

        productImageLabel!!.visibility = View.GONE
    }

    private fun setSelectedCategory(selectedCategory: Category) {
        this.selectedCategory = selectedCategory
        categoryButton!!.text = selectedCategory.title
    }

    @OnClick(R.id.f_add_product_add_button)
    fun onAddButtonClicked() {
        if (productBitmap == null) {
            shakeView(productImageContainer!!)
            return
        }
        val title = productTitleLabel!!.text.toString()
        if (TextUtils.isEmpty(title)) {
            shakeView(productTitleLabel!!.parent as View)
            return
        }
        val producerTitle = producerAutoCompleteView!!.text.toString()
        if (TextUtils.isEmpty(producerTitle)) {
            shakeView(producerAutoCompleteView!!.parent as View)
            return
        }
        if (selectedCategory == null) {
            shakeView(categoryButton!!)
            return
        }

        addProduct(title, producerTitle, productTypeSpinner!!.selectedItem as ProductType, selectedCategory!!)
    }

    private fun addProduct(
            title: String,
            producerTitle: String,
            type: ProductType,
            selectedCategory: Category
    ) {
        showLoadingDialog()
        val producerByTitleIfExists = producerAdapter.getProducerByTitleIfExists(producerTitle)
        val producerSingle = if (producerByTitleIfExists == null)
            producerModel.addProducer(producerTitle)
        else
            Single.just(producerByTitleIfExists)
        producerSingle
                .flatMap { producer -> productModel.addProduct(title, type, code, format, selectedCategory.id, producer.id) }
                .flatMap { product -> productModel.uploadPhoto(product.id, productBitmap!!) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ -> onAddProductSuccess() },
                        { this.onBaseError(it) }
                )
    }

    fun onAddProductSuccess() {
        if (!isAdded) {
            return
        }
        hideLoadingDialog()
        AlertDialog.Builder(activity)
                .setTitle(R.string.succes_dialog_title)
                .setMessage(R.string.add_product_succes_dialog_message)
                .setPositiveButton(
                        android.R.string.ok
                ) { _, _ -> activity.finish() }
                .create()
                .show()
    }

    private fun shakeView(view: View) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
    }

    //region categories
    @OnClick(R.id.f_add_product_category_button)
    fun onCategoryClicked() {
        if (fragmentManager.findFragmentByTag(CHOOSE_CATEGORY_DIALOG_FRAGMENT_KEY) != null) {
            return
        }

        showLoadingDialog()
        val subscription = categoryModel.topCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { ArrayList<Category>(it) }
                .subscribe(
                        { categories ->
                            Timber.i("Categories loaded: %s", categories)
                            hideLoadingDialog()
                            showCategoriesDialog(categories)
                        },
                        { this.onBaseError(it) }
                )
        unsubscribeOnDestroyView(subscription)
    }

    private fun showCategoriesDialog(categories: List<Category>) {
        val complainProductDialogFragment = ChooseCategoryDialogFragmentBuilder(ArrayList(categories)).build()
        complainProductDialogFragment.show(fragmentManager, CHOOSE_CATEGORY_DIALOG_FRAGMENT_KEY)
        complainProductDialogFragment.setListener(
                { position: Int ->
                    val selectedCategory = categories[position]
                    if (selectedCategory.isLowLevel) {
                        showDialogWithChildCategoriesOrReturn(selectedCategory)
                    } else {
                        showCategoriesDialog(selectedCategory.subCategories!!)
                    }
                }
        )
    }

    private fun showDialogWithChildCategoriesOrReturn(category: Category) {
        showLoadingDialog()
        val subscription = categoryModel.getCategory(category.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            hideLoadingDialog()
                            Timber.i("Category loaded: %s", result)
                            if (result.isLowLevel) {
                                setSelectedCategory(category)
                            } else {
                                showCategoriesDialog(result.subCategories!!)
                            }
                        },
                         { this.onBaseError(it) }
                )
        unsubscribeOnDestroyView(subscription)
    }
    //endregion

    //region permissions

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnShowRationale(READ_EXTERNAL_STORAGE)
    internal fun onShowPermissionRationale(permissionRequest: PermissionRequest) {
        AlertDialog.Builder(activity)
                .setTitle(R.string.permision_storage_rationale_dialog_title)
                .setMessage(R.string.permision_rationale_dialog_message)
                .setPositiveButton(android.R.string.ok) { dialog, which -> permissionRequest.proceed() }
                .setNegativeButton(android.R.string.cancel) { dialog, which -> permissionRequest.cancel() }
                .create()
                .show()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnPermissionDenied(READ_EXTERNAL_STORAGE)
    internal fun onPermissionDenied() {
        AlertDialog.Builder(activity)
                .setTitle(R.string.permision_denied_dialog_title)
                .setMessage(R.string.permision_storage_denied_dialog_message)
                .setPositiveButton(R.string.permision_denied_dialog_positive) { dialog, which -> goToPermissionSettings() }
                .create()
                .show()
    }

    protected fun goToPermissionSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    companion object {

        val MAX_SIZE = 1024

        val CAMERA_REQUEST_CODE = 161

        val GALLERY_REQUEST_CODE = 666

        val CHOOSE_CATEGORY_DIALOG_FRAGMENT_KEY = "CHOOSE_CATEGORY_DIALOG_FRAGMENT"
    }
    //endregion
}