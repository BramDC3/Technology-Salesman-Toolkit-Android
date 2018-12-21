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

class RegistrationViewModel : InjectedViewModel() {

    private val _firstname = MutableLiveData<String>()
    val firstname: MutableLiveData<String>
        get() = _firstname

    private val _familyname = MutableLiveData<String>()
    val familyname: MutableLiveData<String>
        get() = _familyname

    private val _email = MutableLiveData<String>()
    val email: MutableLiveData<String>
        get() = _email

    private val _password = MutableLiveData<String>()
    val password: MutableLiveData<String>
        get() = _password

    private val _repeatPassword = MutableLiveData<String>()
    val repeatPassword: MutableLiveData<String>
        get() = _repeatPassword

    val goToLoginClicked = SingleLiveEvent<Any>()

    val showPrivacyPolicyDialog = SingleLiveEvent<Any>()

    val registrationErrorOccured = SingleLiveEvent<Int>()

    val firebaseAccountCreation = SingleLiveEvent<BaseCommand>()

    init {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        password.value = ""
        repeatPassword.value = ""
    }

    fun isRegistrationFormValid() {
        if (!everyFieldHasValue(listOf(firstname.value!!, familyname.value!!, email.value!!, password.value!!, repeatPassword.value!!))) {
            registrationErrorOccured.value = R.string.error_empty_fields
            return
        }

        if (!isEmailValid(email.value!!)) {
            registrationErrorOccured.value = R.string.error_invalid_email
            return
        }

        if (!isPasswordValid(password.value!!)) {
            registrationErrorOccured.value = R.string.error_invalid_password
            return
        }

        if (!passwordsMatch(password.value!!, repeatPassword.value!!)) {
            registrationErrorOccured.value = R.string.error_passwords_dont_match
            return
        }

        showPrivacyPolicyDialog.call()
    }

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
                                        firebaseAccountCreation.value = BaseCommand.Success(R.string.message_account_created)
                                        goToLoginClicked.call()
                                    } else { firebaseAccountCreation.value = BaseCommand.Error(R.string.error_account_not_created) }
                                }
                    } else { firebaseAccountCreation.value = BaseCommand.Error(R.string.error_account_not_created) }
                }
    }

    fun goToLogin() { goToLoginClicked.call() }

    fun clearRegistrationForm() {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        password.value = ""
        repeatPassword.value = ""
    }

}