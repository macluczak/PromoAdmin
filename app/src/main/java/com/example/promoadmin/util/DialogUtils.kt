package com.example.promoadmin.util
import android.app.AlertDialog
import android.content.Context

object DialogUtils {

    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        onConfirm: () -> Unit,
        onCancel: () -> Unit
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("Delete") { _, _ ->
            onConfirm.invoke()
        }

        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
            onCancel.invoke()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
