package com.estasvegano.android.estasvegano.ui

import android.support.v4.util.ArrayMap
import timber.log.Timber


class NonConfigurationObjectsStoreImpl : NonConfigurationObjectsStore {

    private val customStateMap = ArrayMap<String, Any>(3)

    override fun putToNonConfigurationStore(key: String, `object`: Any) {
        val oldValue = customStateMap.put(key, `object`)
        if (oldValue != null) {
            Timber.w("Store already has an object with key = $key. oldValue = $oldValue, newValue = $`object`")
        }
    }

    override fun <T> getFromNonConfigurationStore(key: String): T? {
        val o = customStateMap[key] ?: return null
        try {
            @Suppress("UNCHECKED_CAST") // checked
            return o as T
        } catch (e: ClassCastException) {
            Timber.e(e, "Object with key = $key have a wrong type")
            throw IllegalStateException("Object with key = $key have a wrong type")
        }
    }

    override fun removeFromNonConfigurationStore(key: String) {
        customStateMap.remove(key)
    }
}