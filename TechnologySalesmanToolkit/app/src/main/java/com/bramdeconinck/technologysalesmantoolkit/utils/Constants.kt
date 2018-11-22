package com.bramdeconinck.technologysalesmantoolkit.utils

import java.util.regex.Pattern

val validEmailAddressRegex: Pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)