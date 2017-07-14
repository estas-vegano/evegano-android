package com.estasvegano.android.estasvegano.entity

import android.os.Parcel
import android.os.Parcelable

data class Category(
        val id: Long,
        val title: String,
        val subCategories: List<Category>?,
        val parent: Category?
) : Parcelable {

    val isLowLevel: Boolean
        get() = subCategories?.isNotEmpty() ?: true

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.createTypedArrayList(Category.CREATOR),
            source.readParcelable<Category>(Category::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeTypedList(subCategories)
        dest.writeParcelable(parent, 0)
    }
}
