package com.estasvegano.android.estasvegano.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class BaseActivity : AppCompatActivity(), NonConfigurationObjectsStore {

    lateinit var nonConfigurationObjectsStoreImpl: NonConfigurationObjectsStoreImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        // we need to init store before super.onCreate in order fragments could use onCreate to get objects from store
        nonConfigurationObjectsStoreImpl = lastCustomNonConfigurationInstance as (NonConfigurationObjectsStoreImpl)
        @Suppress("SENSELESS_COMPARISON") // lateinit
        if (nonConfigurationObjectsStoreImpl == null) {
            nonConfigurationObjectsStoreImpl = NonConfigurationObjectsStoreImpl()
        }

        super.onCreate(savedInstanceState)
    }


    override fun onRetainCustomNonConfigurationInstance(): Any {
        return nonConfigurationObjectsStoreImpl
    }

    override fun putToNonConfigurationStore(key: String, `object`: Any) {
        nonConfigurationObjectsStoreImpl.putToNonConfigurationStore(key, `object`)
    }

    override fun <T> getFromNonConfigurationStore(key: String): T? {
        return nonConfigurationObjectsStoreImpl.getFromNonConfigurationStore(key)
    }

    override fun removeFromNonConfigurationStore(key: String) {
        nonConfigurationObjectsStoreImpl.removeFromNonConfigurationStore(key)
    }
}