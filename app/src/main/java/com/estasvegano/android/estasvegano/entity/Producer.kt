package com.estasvegano.android.estasvegano.entity

import android.os.Parcel
import android.os.Parcelable

data class Producer(
        val id: Long,
        val title: String,
        val ethical: Boolean?
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Producer> = object : Parcelable.Creator<Producer> {
            override fun createFromParcel(source: Parcel): Producer = Producer(source)
            override fun newArray(size: Int): Array<Producer?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeValue(ethical)
    }
}