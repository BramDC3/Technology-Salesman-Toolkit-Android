package com.bramdeconinck.technologysalesmantoolkit.utils

object StringUtils {

    @JvmStatic
    // Retrieves the first name of a full name of a firebase user
    fun getFirstName(name: String): String {
        return name.substring(0, name.indexOf(' '))
    }

    @JvmStatic
    // Retrieves the family name of a full name of a firebase user
    fun getFamilyName(name: String): String {
        return name.substring(name.indexOf(' ') + 1)
    }

}