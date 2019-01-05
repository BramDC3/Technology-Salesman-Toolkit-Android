package com.bramdeconinck.technologysalesmantoolkit.utils

import java.util.regex.Pattern

/**
 * A list of constant values.
 */

val validEmailAddressRegex: Pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
const val TEST_WEBSITE: String = "https://bramdeconinck.com"
const val PRIVACY_POLICY: String = "https://technology-salesman-toolkit.firebaseapp.com/privacy_policy.html"
const val GOOGLE_SIGN_IN_REQUEST_CODE: Int = 1
const val SHARED_PREFERENCES_KEY_THEME = "Theme"
const val INSTRUCTION_ITEM = "instructionItem"