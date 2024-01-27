package com.example.promoadmin.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.promoadmin.R

fun loadImageWithGlide(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.image_24)
                .error(R.drawable.image_24)
                .fitCenter()
        )
        .into(imageView)
}