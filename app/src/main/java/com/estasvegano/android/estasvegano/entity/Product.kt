package com.estasvegano.android.estasvegano.entity

import android.os.Parcel
import android.os.Parcelable

data class Product(
        val id: Long,
        val title: String,
        val info: ProductType,
        val photo: String?,
        val producer: Producer,
        val category: Category,
        val codes: List<Code>
) : Parcelable {

    data class Code(val code: String, val type: String) : Parcelable {

        companion object {
            @JvmField val CREATOR: Parcelable.Creator<Code> = object : Parcelable.Creator<Code> {
                override fun createFromParcel(source: Parcel): Code = Code(source)
                override fun newArray(size: Int): Array<Code?> = arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
                source.readString(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(code)
            dest.writeString(type)
        }
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product = Product(source)
            override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            ProductType.fromId(source.readInt()),
            source.readString(),
            source.readParcelable<Producer>(Producer::class.java.classLoader),
            source.readParcelable<Category>(Category::class.java.classLoader),
            source.createTypedArrayList(Code.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeInt(info.id)
        dest.writeString(photo)
        dest.writeParcelable(producer, 0)
        dest.writeParcelable(category, 0)
        dest.writeTypedList(codes)
    }
}
