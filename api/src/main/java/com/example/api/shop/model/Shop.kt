package com.example.api.shop.model

import android.os.Parcel
import android.os.Parcelable
import com.example.api.product.model.Product
import java.util.Date
import java.util.UUID

data class Shop(
    var id: UUID?,
    var name: String,
    var description: String,
    var image: String,
    var locationCode: String,
    var products: List<Product>,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as UUID?,
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        mutableListOf<Product>().apply {
            parcel.readList(this, Product::class.java.classLoader)
        },
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(locationCode)
        parcel.writeList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Shop> {
        override fun createFromParcel(parcel: Parcel): Shop {
            return Shop(parcel)
        }

        override fun newArray(size: Int): Array<Shop?> {
            return arrayOfNulls(size)
        }
    }
}

data class ShopRequest(
    var name: String,
    var description: String,
    var image: String,
    var locationCode: String,
)
