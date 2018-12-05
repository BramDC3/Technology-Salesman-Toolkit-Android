package com.bramdeconinck.technologysalesmantoolkit.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

object MessageUtils {

    @JvmStatic
    // Shows a toast on the screen for a chosen amount of time with a given message
    fun makeToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) { Toast.makeText(context, message, duration).show() }

    @JvmStatic
    // Show a dialog on the screen with a given message
    fun showDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Oké") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

}