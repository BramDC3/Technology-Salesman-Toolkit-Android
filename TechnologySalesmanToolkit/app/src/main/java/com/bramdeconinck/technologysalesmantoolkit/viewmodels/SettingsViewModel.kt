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
     * Injection of the [firestoreAPI] and [sharedPreferences].
     */
    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    /**
     * Value that indicates whether dark mode is enabled or not.
     */
    private val _isDarkModeEnabled = MutableLiveData<Boolean>()
    val isDarkModeEnabled: LiveData<Boolean>
        get() = _isDarkModeEnabled

    /**
     * [SingleLiveEvent] objects used to emit events.
     */
    val visitWebsiteClicked = SingleLiveEvent<Any>()
    val visitPrivacyPolicyClicked = SingleLiveEvent<Any>()
    val makeSuggestionClicked = SingleLiveEvent<Any>()
    val showSignOutDialogClicked = SingleLiveEvent<Any>()
    val emptySuggestion = SingleLiveEvent<Any>()
    val suggestionCallback = SingleLiveEvent<BaseCommand>()
    val signOutTriggered = SingleLiveEvent<Any>()

    init {
        val theme = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_THEME, 1)
        _isDarkModeEnabled.value = theme == 2
    }

    fun visitWebsite() { visitWebsiteClicked.call() }

    fun toggleDarkMode() { _isDarkModeEnabled.value = !_isDarkModeEnabled.value!! }

    fun visitPrivacyPolicy() { visitPrivacyPolicyClicked.call() }

    fun makeSuggestion() { makeSuggestionClicked.call() }

    fun showSignOutDialog() { showSignOutDialogClicked.call() }

    /**
     * Function that is gives as a parameter in a dialog that retrieves a String.
     */
    fun getSuggestion() = { suggestion: String -> validateSuggestion(suggestion)}

    /**
     * Function to validate a suggestion.
     */
    private fun validateSuggestion(suggestion: String) {
        if (everyFieldHasValue(listOf(suggestion))) firestoreAPI.postSuggestion(this, suggestion)
        else emptySuggestion.call()
    }

    fun signOut() = {
        firebaseAuth.signOut()
        signOutTriggered.call()
    }

    override fun showSuccessMessage() { suggestionCallback.value = BaseCommand.Success(R.string.send_suggestion_succes) }

    override fun showFailureMessage() { suggestionCallback.value = BaseCommand.Error(R.string.send_suggestion_error) }

}