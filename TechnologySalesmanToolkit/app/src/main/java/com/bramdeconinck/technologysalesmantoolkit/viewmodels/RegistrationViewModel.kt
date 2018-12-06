package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.Event
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.createProfileUpdates
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isPasswordValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.passwordsMatch
import javax.inject.Inject

class RegistrationViewModel : InjectedViewModel() {

    @Inject
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    private val _navigateToLogin = MutableLiveData<Event<String>>()

    val navigateToLogin : LiveData<Event<String>>
        get() = _navigateToLogin

    private val _showPrivacyPolicyDialog = MutableLiveData<Event<String>>()

    val showPrivacyPolicyDialog : LiveData<Event<String>>
        get() = _showPrivacyPolicyDialog

    fun validateRegistrationForm(firstname: String, familyname: String, email: String, password: String, repeatPassword: String) {
        if (!everyFieldHasValue(listOf(firstname, familyname, email, password, repeatPassword))) {
            makeToast(context, context.getString(R.string.error_empty_fields))
            return
        }

        if (!isEmailValid(email)) {
            makeToast(context, context.getString(R.string.error_invalid_email))
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

        _showPrivacyPolicyDialog.value = Event("")
    }

    fun createFirebaseAccount(firstname: String, familyname: String, email: String, password: String): () -> Unit = {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task1 ->
                    if (task1.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        firebaseUser!!.updateProfile(createProfileUpdates(firstname, familyname))
                                .addOnCompleteListener { task2 ->
                                    if (task2.isSuccessful) {
                                        firebaseUser!!.sendEmailVerification()
                                        firebaseAuth.signOut()
                                        makeToast(context, context.getString(R.string.message_account_created))
                                        _navigateToLogin.value = Event("")
                                    } else { makeToast(context, context.getString(R.string.error_account_not_created)) }
                                }
                    } else { makeToast(context, context.getString(R.string.error_account_not_created)) }
                }
    }

}