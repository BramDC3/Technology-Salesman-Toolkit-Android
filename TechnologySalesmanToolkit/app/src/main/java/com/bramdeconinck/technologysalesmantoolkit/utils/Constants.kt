package com.bramdeconinck.technologysalesmantoolkit.utils

import java.util.regex.Pattern

val validEmailAddressRegex: Pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
const val website: String = "https://bramdeconinck.com"
const val privacyPolicy: String = "https://technology-salesman-toolkit.firebaseapp.com/privacy_policy.html"
const val RC_SIGN_IN: Int = 1