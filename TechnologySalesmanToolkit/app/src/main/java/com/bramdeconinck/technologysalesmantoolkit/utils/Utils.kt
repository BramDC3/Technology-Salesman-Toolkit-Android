package com.bramdeconinck.technologysalesmantoolkit.utils

import android.content.Context
import android.widget.Toast
import java.util.regex.Pattern

object Utils {

    private val validEmailAddressRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    @JvmStatic
    // Show a message on the screen for a chosen amount of time
    fun makeToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

    @JvmStatic
    // Check whether or not the entered string is a valid email address
    fun isValid(email: String): Boolean {
        val matcher = validEmailAddressRegex.matcher(email)
        return matcher.find()
    }
}