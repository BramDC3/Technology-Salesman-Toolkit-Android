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
     * [firstname] is the first name of the user.
     */
    val firstname = MutableLiveData<String>()

    /**
     * [familyname] is the family name of the user.
     */
    val familyname = MutableLiveData<String>()

    /**
     * [email] is the email address of the user.
     */
    val email = MutableLiveData<String>()

    /**
     * [password] is the password the user wants.
     */
    val password = MutableLiveData<String>()

    /**
     * [repeatPassword] is the password the user wants, repeated.
     */
    val repeatPassword = MutableLiveData<String>()

    /**
     * [SingleLiveEvent] that indicates the user wants to go to the Login Fragment.
     */
    val goToLoginClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] that indicates the user has correctly filled out the registration form
     * and needs to be shown the privacy policy dialog.
     */
    val showPrivacyPolicyDialog = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] that indicates an error occurred when validating the registration form.
     * It has the value of a String resource Id.
     */
    val registrationEvent = SingleLiveEvent<Int>()

    init {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        password.value = ""
        repeatPassword.value = ""
    }

    /**
     * Function that gets called when the Login TextView is clicked.
     */
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

}