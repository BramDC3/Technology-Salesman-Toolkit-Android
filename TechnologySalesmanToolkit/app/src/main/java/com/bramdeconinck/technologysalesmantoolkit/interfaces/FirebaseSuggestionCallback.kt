package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used for handling callbacks when posting a suggestion.
 */
interface FirebaseSuggestionCallback {
    fun showSuccessMessage()
    fun showFailureMessage()
}