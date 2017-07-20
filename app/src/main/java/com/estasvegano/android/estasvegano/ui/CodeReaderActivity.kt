package com.estasvegano.android.estasvegano.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.estasvegano.android.estasvegano.R
import com.estasvegano.android.estasvegano.entity.Product
import com.estasvegano.android.estasvegano.ui.util.findOrThrow
import timber.log.Timber

class CodeReaderActivity : AppCompatActivity(), CodeReaderFragment.OnCodeReadedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_code_readed)

        var fragment: CodeReaderFragment? = supportFragmentManager.findOrThrow(R.id.container)

        if (fragment == null) {
            fragment = CodeReaderFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }
    }

    override fun onProductLoaded(product: Product) {
        Timber.i("Going to view product activity with product: %s", product)
        startActivity(ViewProductActivity.getStartIntent(this, product))
    }

    override fun onNoSuchProduct(code: String, format: String) {
        Timber.i("Going to add product activity with code: %s format: %s", code, format)
        startActivity(AddProductActivity.getStartIntent(this, code, format))
    }
}
