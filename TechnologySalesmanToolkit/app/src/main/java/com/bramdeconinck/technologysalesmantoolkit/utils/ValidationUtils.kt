package com.bramdeconinck.technologysalesmantoolkit.utils

object ValidationUtils {

    @JvmStatic
    // Validates whether every field of a form has a value
    fun everyFieldHasValue(fields: List<String>): Boolean {
        fields.forEach { if (it.isBlank()) return false }
        return true
    }

    @JvmStatic
    // Validates whether every field of a form has a value
    fun atLeastOneFieldChanged(fields: Map<String, String>): Boolean {
        var counter = 0
        fields.forEach { if (it.key == it.value) counter++ }
        return (counter < fields.size)
    }

    @JvmStatic
    // Validates whether or not the entered string is a valid email address
    fun isEmailValid(email: String): Boolean {
        val matcher = validEmailAddressRegex.matcher(email)
        return matcher.find()
    }

    @JvmStatic
    // Validates whether the chosen password is valid
    fun isPasswordValid(password: String): Boolean { return (password.length >= 6) }

    @JvmStatic
    // Validates whether two entered passwords match
    fun passwordsMatch(password: String, repeatPassword: String): Boolean { return (password == repeatPassword) }

}