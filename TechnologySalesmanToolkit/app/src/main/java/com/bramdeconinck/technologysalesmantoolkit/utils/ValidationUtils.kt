package com.bramdeconinck.technologysalesmantoolkit.utils

object ValidationUtils {

    @JvmStatic
    // Checks whether or not the entered string is a valid email address
    fun isEmailValid(email: String): Boolean {
        val matcher = validEmailAddressRegex.matcher(email)
        return matcher.find()
    }

}