package com.estasvegano.android.estasvegano.data.web.request

import android.os.Parcel
import android.os.Parcelable

data class AddProducerRequest(val title: String, val ethical: Boolean? = null) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<AddProducerRequest> = object : Parcelable.Creator<AddProducerRequest> {
            override fun createFromParcel(source: Parcel): AddProducerRequest = AddProducerRequest(source)
            override fun newArray(size: Int): Array<AddProducerRequest?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeValue(ethical)
    }
}