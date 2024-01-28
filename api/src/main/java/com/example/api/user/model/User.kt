package com.example.api.user.model

import kotlinx.serialization.Serializable

import android.os.Parcel
import android.os.Parcelable
import com.example.api.shop.model.Shop

@Serializable
data class User(
    val id: String,
    val role: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val password: String,
    val shops: List<Shop>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        mutableListOf<Shop>().apply {
            parcel.readList(this, Shop::class.java.classLoader)
        },
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(role)
        parcel.writeString(email)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(password)
        parcel.writeList(shops)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

data class UserRequest(
    val id: String,
    val email: String,
    val password: String,
    val role: String,
)
