package com.example.promoadmin.util

import android.text.Editable

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String?.isStringNull(): Boolean =
     this.isNullOrBlank() || this.equals("null", ignoreCase = true)
