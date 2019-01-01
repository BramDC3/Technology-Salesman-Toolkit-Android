package com.bramdeconinck.technologysalesmantoolkit.utils

object StringUtils {

    @JvmStatic
    // Retrieves the first name of a full name of a firebase user
    fun getFirstname(name: String): String { return name.substring(0, name.indexOf(' ')) }

    @JvmStatic
    // Retrieves the family name of a full name of a firebase user
    fun getFamilyname(name: String): String { return name.substring(name.indexOf(' ') + 1) }

    @JvmStatic
    fun formatInstructionsList(content: List<String>): String {
        var string = ""
        content.forEachIndexed { index, s -> string += String.format("%d. %s\n\n", index + 1, s) }
        return string
    }

    @JvmStatic
    fun formatPrice(price: Double): String { return String.format("€ %.2f", price) }

}