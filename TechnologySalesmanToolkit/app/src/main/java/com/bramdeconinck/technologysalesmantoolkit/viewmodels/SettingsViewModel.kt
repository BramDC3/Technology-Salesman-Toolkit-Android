package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.SHARED_PREFERENCES_KEY_THEME
import javax.inject.Inject

/**
 * Instance of [InjectedViewModel] that contains data and functions used in the SettingsFragment.
 */
class SettingsViewModel : InjectedViewModel(), FirebaseSuggestionCallback {

    /**
     * Injection of the [FirestoreAPI] using Dagger.
     */
    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    /**
     * Injection of the [sharedPreferences] using Dagger.
     */
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    /**
     * Value that indicates whether dark mode is enabled or not.
     */
    private val _isDarkModeEnabled = MutableLiveData<Boolean>()
    val isDarkModeEnabled: LiveData<Boolean>
        get() = _isDarkModeEnabled

    /**
     * [SingleLiveEvent] indicating that the user wants to visit the website.
     */
    val visitWebsiteClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] indicating that the user wants to see the privacy policy.
     */
    val visitPrivacyPolicyClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] indicating that the user wants to make a suggestion.
     */
    val makeSuggestionClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] indicating that the user wants to sign out.
     */
    val showSignOutDialogClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] indicating that the user didn't enter a suggestion but wanted to send it anyway.
     */
    val emptySuggestion = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] indicating that an event occurred when sending a suggestion.
     * It's value is a [BaseCommand].
     */
    val suggestionCallback = SingleLiveEvent<BaseCommand>()

    /**
     * [SingleLiveEvent] indicating that the user wants to sign out.
     */
    val signOutTriggered = SingleLiveEvent<Any>()

    init {
        val theme = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_THEME, 1)
        _isDarkModeEnabled.value = theme == 2
    }

    /**
     * Function that gets called when the Visit Website button is clicked.
     */
    fun visitWebsite() { visitWebsiteClicked.call() }

    /**
     * Function that gets called when the Dark Mode button is clicked.
     */
    fun toggleDarkMode() { _isDarkModeEnabled.value = !_isDarkModeEnabled.value!! }

    /**
     * Function that gets called when the Show Privacy Policy button is clicked.
     */
    fun visitPrivacyPolicy() { visitPrivacyPolicyClicked.call() }

    /**
     * Function that gets called when the Make A Suggestion button is clicked.
     */
    fun makeSuggestion() { makeSuggestionClicked.call() }

    /**
     * Function that gets triggered when the Sign Out button is clicked.
     */
    fun showSignOutDialog() { showSignOutDialogClicked.call() }

    /**
     * Function that is given as a parameter in a dialog that retrieves the suggestion as a [String].
     */
    fun getSuggestion() = { suggestion: String -> validateSuggestion(suggestion)}

    /**
     * Function to validate a suggestion.
     *
     * @param suggestion: [String] with the suggestion of the user.
     */
    private fun validateSuggestion(suggestion: String) {
        if (everyFieldHasValue(listOf(suggestion))) firestoreAPI.postSuggestion(this, suggestion)
        else emptySuggestion.call()
    }

    /**
     * Function that signs the user out.
     */
    fun signOut() = {
        firebaseAuth.signOut()
        signOutTriggered.call()
    }

    /**
     * Function that gets triggered when the suggestion is successfully posted.
     */
    override fun showSuccessMessage() { suggestionCallback.value = BaseCommand.Success(R.string.send_suggestion_succes) }

    /**
     * Function that gets triggered when an error occurred while posting the suggestion.
     */
    override fun showFailureMessage() { suggestionCallback.value = BaseCommand.Error(R.string.send_suggestion_error) }

}