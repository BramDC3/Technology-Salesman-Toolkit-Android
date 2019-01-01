package com.bramdeconinck.technologysalesmantoolkit.utils

/**
 * [ValidationUtils] contains all the utilities for form validation.
 */
object ValidationUtils {

    /**
     * Validates whether every field of a form has a value.
     */
    @JvmStatic
    fun everyFieldHasValue(fields: List<String>): Boolean {
        fields.forEach { if (it.isBlank()) return false }
        return true
    }

    /**
     * Validates whether at least one field of a form has changed.
     */
    @JvmStatic
    fun atLeastOneFieldChanged(fields: Map<String, String>): Boolean {
        var counter = 0
        fields.forEach { if (it.key == it.value) counter++ }
        return (counter < fields.size)
    }

    /**
     * Validates whether or not the entered string is a valid email address.
     */
    @JvmStatic
    fun isEmailValid(email: String): Boolean {
        val matcher = validEmailAddressRegex.matcher(email)
        return matcher.find()
    }

    /**
     * Validates whether the chosen password is valid or not.
     */
    @JvmStatic
    fun isPasswordValid(password: String): Boolean { return (password.length >= 6) }

    /**
     * Validates whether two entered passwords match or not.
     */
    @JvmStatic
    fun passwordsMatch(password: String, repeatPassword: String): Boolean { return (password == repeatPassword) }

}