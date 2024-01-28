package com.example.promoadmin.util

import android.text.Editable
import java.text.SimpleDateFormat
import java.util.Date

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun String?.isStringNull(): Boolean =
     this.isNullOrBlank() || this.equals("null", ignoreCase = true) ||  this.equals("url", ignoreCase = true)

fun String?.toSimpleDate(): String {
     if (this.isNullOrBlank()) {
          return ""
     }
     val dateFormat = SimpleDateFormat("dd/MM/yy")
     val parsedDate = SimpleDateFormat("yyyy-MM-dd").parse(this)

     return parsedDate?.let { dateFormat.format(it) } ?: ""
}

