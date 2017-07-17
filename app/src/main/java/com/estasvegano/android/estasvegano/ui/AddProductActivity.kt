package com.estasvegano.android.estasvegano.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.estasvegano.android.estasvegano.R

class AddProductActivity : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val code = intent.getStringExtra(CODE_EXTRA_KEY)
        val format = intent.getStringExtra(FORMAT_EXTRA_KEY)

        setContentView(R.layout.activity_view_product)

        var fragment: AddProductFragment? = supportFragmentManager.findFragmentById(R.id.container) as AddProductFragment
        if (fragment == null) {
            fragment = AddProductFragmentBuilder(code, format).build()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }
    }

    companion object {

        val CODE_EXTRA_KEY = "CODE_EXTRA"
        val FORMAT_EXTRA_KEY = "FORMAT_EXTRA"

        fun getStartIntent(context: Context, code: String, format: String): Intent {
            val intent = Intent(context, AddProductActivity::class.java)
            intent.putExtra(CODE_EXTRA_KEY, code)
            intent.putExtra(FORMAT_EXTRA_KEY, format)
            return intent
        }
    }
}
