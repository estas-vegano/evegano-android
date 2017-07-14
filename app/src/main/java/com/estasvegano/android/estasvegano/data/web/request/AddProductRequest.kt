package com.estasvegano.android.estasvegano.data.web.request

import android.os.Parcel
import android.os.Parcelable
import com.estasvegano.android.estasvegano.entity.ProductType

data class AddProductRequest(
        val title: String,
        val info: ProductType,
        val producerId: Long,
        val categoryId: Long,
        val code: String,
        val codeType: String
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<AddProductRequest> = object : Parcelable.Creator<AddProductRequest> {
            override fun createFromParcel(source: Parcel): AddProductRequest = AddProductRequest(source)
            override fun newArray(size: Int): Array<AddProductRequest?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            ProductType.values()[source.readInt()],
            source.readLong(),
            source.readLong(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeInt(info.ordinal)
        dest.writeLong(producerId)
        dest.writeLong(categoryId)
        dest.writeString(code)
        dest.writeString(codeType)
    }
}
