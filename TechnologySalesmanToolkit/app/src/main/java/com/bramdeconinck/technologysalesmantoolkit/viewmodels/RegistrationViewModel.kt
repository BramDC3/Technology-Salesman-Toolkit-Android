package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.Event
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showPrivacyPolicyDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isPasswordValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.passwordsMatch
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject

class RegistrationViewModel : InjectedViewModel() {

    @Inject
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    private val _navigateToLogin = MutableLiveData<Event<String>>()

    val navigateToLogin : LiveData<Event<String>>
        get() = _navigateToLogin

    fun validateRegistrationForm(firstname: String, familyname: String, email: String, password: String, repeatPassword: String) {
        if (!everyFieldHasValue(listOf(firstname, familyname, email, password, repeatPassword))) {
            makeToast(context, context.getString(R.string.error_empty_fields))
            return
        }

        if (!isEmailValid(email)) {
            MessageUtils.makeToast(context, context.getString(R.string.error_invalid_email))
            return
        }

        if (!isPasswordValid(password)) {
            makeToast(context, context.getString(R.string.error_invalid_password))
            return
        }

        if (!passwordsMatch(password, repeatPassword)) {
            makeToast(context, context.getString(R.string.error_passwords_dont_match))
            return
        }

        showPrivacyPolicyDialog(context, context.getString(R.string.title_privacy_policy_dialog), context.getString(R.string.message_privacy_policy_dialog), createFirebaseAccount(firstname, familyname, email, password))
    }

    private fun createFirebaseAccount(firstname: String, familyname: String, email: String, password: String): () -> Unit = {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateUserInfo(firstname, familyname)
                    } else {
                        makeToast(context, context.getString(R.string.error_account_not_created))
                    }
                }
    }

    private fun updateUserInfo(firstname: String, familyname: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("$firstname $familyname")
                .build()

        firebaseUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser!!.sendEmailVerification()
                        firebaseAuth.signOut()
                        makeToast(context, context.getString(R.string.message_account_created))
                        _navigateToLogin.value = Event("")
                    }
                }
    }
}