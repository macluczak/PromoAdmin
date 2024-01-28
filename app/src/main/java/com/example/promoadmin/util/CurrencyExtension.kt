package com.example.promoadmin.util

fun Double.toPLN(): String = String.format("%.2f zł", this)

fun Double.priceToText(): String = String.format("%.2f", this)