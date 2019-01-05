package com.bramdeconinck.technologysalesmantoolkit.utils

/**
 * [StringUtils] contains all the utilities to manipulate [String].
 */
object StringUtils {

    /**
     * Retrieves the first name of a full name of a Firebase user.
     *
     * @param name: Full name of the current user.
     * @return: First name of the current user.
     */
    @JvmStatic
    fun getFirstname(name: String): String { return name.substring(0, name.indexOf(' ')) }

    /**
     * Retrieves the family name of a full name of a Firebase user.
     *
     * @param name: Full name of the current user.
     * @return: Family name of the current user.
     */
    @JvmStatic
    fun getFamilyname(name: String): String { return name.substring(name.indexOf(' ') + 1) }

    /**
     * Format a list of instructions to be able to be shown in a single TextView.
     *
     * @param content: List of [String]
     * @return: Formatted [String].
     */
    @JvmStatic
    fun formatInstructionsList(content: List<String>): String {
        var string = ""
        content.forEachIndexed { index, s -> string += String.format("%d. %s\n\n", index + 1, s) }
        return string
    }

    /**
     * Format a price to have a € sign and round it to two decimals.
     *
     * @param price: [Double]
     * @return: [price] as a formatted [String].
     */
    @JvmStatic
    fun formatPrice(price: Double): String { return String.format("€ %.2f", price) }

}