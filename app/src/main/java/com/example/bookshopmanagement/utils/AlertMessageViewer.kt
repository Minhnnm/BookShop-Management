package com.example.bookshopmanagement.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly

object AlertMessageViewer {
    fun showAlertDialogMessage(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle(null)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}