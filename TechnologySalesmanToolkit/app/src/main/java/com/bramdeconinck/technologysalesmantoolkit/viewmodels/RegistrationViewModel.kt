package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.createProfileUpdates
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isPasswordValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.passwordsMatch

/**
 * Instance of [InjectedViewModel] that contains data and functions used in the ProfileFragment.
 */
class RegistrationViewModel : InjectedViewModel() {

    /**
     * Values for fields of the registration form.
     */
    val firstname = MutableLiveData<String>()
    val familyname = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

    /**
     * [SingleLiveEvent] objects used to emit events.
     */
    val goToLoginClicked = SingleLiveEvent<Any>()
    val showPrivacyPolicyDialog = SingleLiveEvent<Any>()
    val registrationEvent = SingleLiveEvent<Int>()

    init {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        password.value = ""
        repeatPassword.value = ""
    }

    fun goToLogin() { goToLoginClicked.call() }

    /**
     * Function to validate the registration form.
     */
    fun isRegistrationFormValid() {
        if (!everyFieldHasValue(listOf(firstname.value!!, familyname.value!!, email.value!!, password.value!!, repeatPassword.value!!))) {
            registrationEvent.value = R.string.error_empty_fields
            return
        }

        if (!isEmailValid(email.value!!)) {
            registrationEvent.value = R.string.error_invalid_email
            return
        }

        if (!isPasswordValid(password.value!!)) {
            registrationEvent.value = R.string.error_invalid_password
            return
        }

        if (!passwordsMatch(password.value!!, repeatPassword.value!!)) {
            registrationEvent.value = R.string.error_passwords_dont_match
            return
        }

        showPrivacyPolicyDialog.call()
    }

    /**
     * Function to create a Firebase account with an email address and password combination.
     */
    fun createFirebaseAccount(): () -> Unit = {
        firebaseAuth.createUserWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener { task1 ->
                    if (task1.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        firebaseUser!!.updateProfile(createProfileUpdates(firstname.value!!, familyname.value!!))
                                .addOnCompleteListener { task2 ->
                                    if (task2.isSuccessful) {
                                        firebaseUser!!.sendEmailVerification()
                                        firebaseAuth.signOut()
                                        registrationEvent.value = R.string.message_account_created
                                        goToLoginClicked.call()
                                    } else { registrationEvent.value = R.string.error_account_not_created }
                                }
                    } else { registrationEvent.value = R.string.error_account_not_created }
                }
    }

    fun clearRegistrationForm() {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        password.value = ""
        repeatPassword.value = ""
    }

}