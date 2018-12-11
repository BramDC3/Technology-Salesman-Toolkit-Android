package com.bramdeconinck.technologysalesmantoolkit.viewmodels

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

    val navigateToLogin = SingleLiveEvent<Any>()

    val showPrivacyPolicyDialog = SingleLiveEvent<Any>()

    val registrationFormValidation = SingleLiveEvent<BaseCommand>()

    val firebaseAccountCreation = SingleLiveEvent<BaseCommand>()

    fun isRegistrationFormValid(firstname: String, familyname: String, email: String, password: String, repeatPassword: String) {
        if (!everyFieldHasValue(listOf(firstname, familyname, email, password, repeatPassword))) {
            registrationFormValidation.value = BaseCommand.Error(R.string.error_empty_fields)
            return
        }

        if (!isEmailValid(email)) {
            registrationFormValidation.value = BaseCommand.Error(R.string.error_invalid_email)
            return
        }

        if (!isPasswordValid(password)) {
            registrationFormValidation.value = BaseCommand.Error(R.string.error_invalid_password)
            return
        }

        if (!passwordsMatch(password, repeatPassword)) {
            registrationFormValidation.value = BaseCommand.Error(R.string.error_passwords_dont_match)
            return
        }

        showPrivacyPolicyDialog.call()
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
                                        firebaseAccountCreation.value = BaseCommand.Success(R.string.message_account_created)
                                        navigateToLogin.call()
                                    } else { firebaseAccountCreation.value = BaseCommand.Error(R.string.error_account_not_created) }
                                }
                    } else { firebaseAccountCreation.value = BaseCommand.Error(R.string.error_account_not_created) }
                }
    }

}