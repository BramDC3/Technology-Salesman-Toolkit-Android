package com.bramdeconinck.technologysalesmantoolkit.utils

import android.app.AlertDialog
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import java.util.regex.Pattern

object Utils {

    private val validEmailAddressRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    @JvmStatic
    // Shows a toast on the screen for a chosen amount of time
    fun makeToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

    @JvmStatic
    // Show a snackbar on the screen for a chosen amount of time
    fun makeSnackBar(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view, message, duration).show()
    }

    @JvmStatic
    // Show a dialog on the screen
    fun showDialog(context: Context, title: String, message: String) {
        val alertDialog: AlertDialog
        alertDialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Oké") { dialog, _ -> dialog?.dismiss() }
                .create()

        alertDialog.show()
    }

    @JvmStatic
    // Checks whether or not the entered string is a valid email address
    fun isEmailValid(email: String): Boolean {
        val matcher = validEmailAddressRegex.matcher(email)
        return matcher.find()
    }
}