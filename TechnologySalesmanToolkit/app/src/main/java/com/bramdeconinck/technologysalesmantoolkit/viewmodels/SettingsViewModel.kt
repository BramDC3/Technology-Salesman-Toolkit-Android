package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import javax.inject.Inject

class SettingsViewModel : InjectedViewModel(), IFirebaseSuggestionCallback {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    private val _isDarkModeEnabled = MutableLiveData<Boolean>()
    val isDarkModeEnabled: MutableLiveData<Boolean>
        get() = _isDarkModeEnabled

    val visitWebsiteClicked = SingleLiveEvent<Any>()

    val visitPrivacyPolicyClicked = SingleLiveEvent<Any>()

    val makeSuggestionClicked = SingleLiveEvent<Any>()

    val showSignOutDialogClicked = SingleLiveEvent<Any>()

    val emptySuggestion = SingleLiveEvent<Any>()

    val suggestionCallback = SingleLiveEvent<BaseCommand>()

    val signOutTriggered = SingleLiveEvent<Any>()

    init {
        _isDarkModeEnabled.value = false
    }

    fun visitWebsite() { visitWebsiteClicked.call() }

    fun toggleDarkMode() { _isDarkModeEnabled.value = !_isDarkModeEnabled.value!! }

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

    override fun showSuccesMessage() { suggestionCallback.value = BaseCommand.Success(R.string.send_suggestion_succes) }

    override fun showFailureMessage() { suggestionCallback.value = BaseCommand.Error(R.string.send_suggestion_error) }

}