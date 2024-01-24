package com.example.api.product.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Product(
    val id: UUID? = null,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val amount: Int,
    val category: String,
    val maker: String,
    val discountPrice: Double? = null,
    val discountDate: String? = null, // Format ISO 8601
    val shopId: UUID? = null,
    val createdAt: String? = null, // Format ISO 8601
    val updatedAt: String? = null // Format ISO 8601
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as UUID?,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readSerializable() as Double?,
        parcel.readString(),
        parcel.readSerializable() as UUID?,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeString(image)
        parcel.writeInt(amount)
        parcel.writeString(category)
        parcel.writeString(maker)
        parcel.writeSerializable(discountPrice)
        parcel.writeString(discountDate)
        parcel.writeSerializable(shopId)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
