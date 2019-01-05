package com.bramdeconinck.technologysalesmantoolkit.utils

/**
 * [ValidationUtils] contains all the utilities for form validation.
 */
object ValidationUtils {

    /**
     * Validates whether every field of a form has a value.
     *
     * @param fields: List of [String]
     * @return: [Boolean] indicating whether every [String] has a value or not.
     */
    @JvmStatic
    fun everyFieldHasValue(fields: List<String>): Boolean {
        fields.forEach { if (it.isBlank()) return false }
        return true
    }

    /**
     * Validates whether at least one field of a form has changed.
     *
     * @param fields: Map with a [String] as key and a [String] as value.
     * @return: [Boolean] indicating whether at least one key has a different value than it's value or not.
     */
    @JvmStatic
    fun atLeastOneFieldChanged(fields: Map<String, String>): Boolean {
        var counter = 0
        fields.forEach { if (it.key == it.value) counter++ }
        return (counter < fields.size)
    }

    /**
     * Validates whether or not the entered string is a valid email address.
     *
     * @param email: Potential email address as a [String].
     * @return: [Boolean] indicating whether the potential email address is valid or not.
     */
    @JvmStatic
    fun isEmailValid(email: String): Boolean {
        val matcher = validEmailAddressRegex.matcher(email)
        return matcher.find()
    }

    /**
     * Validates whether the chosen password is valid or not.
     *
     * @param password: Potential password as a [String].
     * @return: [Boolean] indicating whether the potential password is valid or not.
     */
    @JvmStatic
    fun isPasswordValid(password: String): Boolean { return (password.length >= 6) }

    /**
     * Validates whether two entered passwords match or not.
     *
     * @param password: Potential password as a [String].
     * @param repeatPassword: A [String] with supposedly the same value as [password].
     * @return [Boolean] indicating whether the value of [password] and the value of [repeatPassword] are equal.
     */
    @JvmStatic
    fun passwordsMatch(password: String, repeatPassword: String): Boolean { return (password == repeatPassword) }

}