package com.bramdeconinck.technologysalesmantoolkit.viewmodels

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

class SettingsViewModel : InjectedViewModel(), FirebaseSuggestionCallback {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val isDarkModeEnabled = MutableLiveData<Boolean>()

    val visitWebsiteClicked = SingleLiveEvent<Any>()

    val visitPrivacyPolicyClicked = SingleLiveEvent<Any>()

    val makeSuggestionClicked = SingleLiveEvent<Any>()

    val showSignOutDialogClicked = SingleLiveEvent<Any>()

    val emptySuggestion = SingleLiveEvent<Any>()

    val suggestionCallback = SingleLiveEvent<BaseCommand>()

    val signOutTriggered = SingleLiveEvent<Any>()

    init {
        val theme = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_THEME, 1)
        isDarkModeEnabled.value = theme == 2
    }

    fun visitWebsite() { visitWebsiteClicked.call() }

    fun toggleDarkMode() { isDarkModeEnabled.value = !isDarkModeEnabled.value!! }

    fun visitPrivacyPolicy() { visitPrivacyPolicyClicked.call() }

    fun makeSuggestion() { makeSuggestionClicked.call() }

    fun showSignOutDialog() { showSignOutDialogClicked.call() }

    fun getSuggestion() = { suggestion: String -> validateSuggestion(suggestion)}

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