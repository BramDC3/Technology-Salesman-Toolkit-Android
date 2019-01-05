package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used for handling callbacks when posting a suggestion.
 */
interface FirebaseSuggestionCallback {

    /**
     * Function to show a message when the suggestion has been posted successfully.
     */
    fun showSuccessMessage()

    /**
     * Function to show a message when an error occurred while posting the suggestion.
     */
    fun showFailureMessage()
}