package com.bramdeconinck.technologysalesmantoolkit.utils

import java.util.regex.Pattern

val validEmailAddressRegex: Pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
const val website: String = "https://bramdeconinck.com"
const val privacyPolicy: String = "https://technology-salesman-toolkit.firebaseapp.com/privacy_policy.html"
const val GOOGLE_SIGN_IN_REQUEST_CODE: Int = 1
const val sharedPreferencesThemeKey = "Theme"
const val SERVICE_ITEM = "serviceItem"
const val INSTRUCTION_ITEM = "instructionItem"