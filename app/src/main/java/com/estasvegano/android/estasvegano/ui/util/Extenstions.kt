package com.estasvegano.android.estasvegano.ui.util

import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.estasvegano.android.estasvegano.ui.BaseFragment

fun BaseFragment.assertActivityImplementsInterface(activity: Context, clazz: Class<out Any>) {
    if (activity.javaClass.isAssignableFrom(clazz)) {
        throw IllegalStateException("Owner activity should implement ${clazz.canonicalName}, was: " + activity.javaClass.canonicalName)
    }
}

inline fun <reified T : View> BaseFragment.viewById(@IdRes id: Int): T {
    val localView = view ?: throw IllegalStateException("View is not set!")
    return localView.findViewById(id) as T
}

inline fun <reified T : View> BaseFragment.lazyView(@IdRes id: Int): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE, { viewById<T>(id) })

inline fun <reified T : Fragment> FragmentManager.findOrThrow(tag: String): T {
    val fragment = findFragmentByTag(tag) as? T
    return fragment ?: throw IllegalStateException("Fragment with tag $tag not found or has different class: $fragment")
}

inline fun <reified T : Fragment> FragmentManager.findOrThrow(@IdRes id: Int): T {
    val fragment = findFragmentById(id) as? T
    return fragment ?: throw IllegalStateException("Fragment with id $id not found or has different class: $fragment")
}