package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import javax.inject.Inject

class SettingsViewModel : InjectedViewModel(), IFirebaseSuggestionCallback {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    fun getSuggestion() = { suggestion: String -> validateSuggestion(suggestion)}

    private fun validateSuggestion(suggestion: String) {
        if (everyFieldHasValue(listOf(suggestion))) firestoreAPI.postSuggestion(this, suggestion)
        else makeToast(R.string.send_suggestion_empty_error)
    }

    override fun showSuccesMessage() { makeToast(R.string.send_suggestion_succes) }

    override fun showFailureMessage() { makeToast(R.string.send_suggestion_error) }
}