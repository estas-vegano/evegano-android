package com.estasvegano.android.estasvegano.ui

import android.support.annotation.UiThread

/**
 * Interface that informs that {@link android.app.Activity} has ability to save object between rotations.
 */
@UiThread
interface NonConfigurationObjectsStore {

    /**
     * Puts object to non-configuration store. You could get object from this store later.
     */
    fun putToNonConfigurationStore(key: String, `object`: Any)

    /**
     * @return {@link Object} or <code>null</code> if no objects with given name have been founded.
     */
    fun <T> getFromNonConfigurationStore(key: String): T?

    /**
     * Removes object with given key from store.
     */
    fun removeFromNonConfigurationStore(key: String)
}
