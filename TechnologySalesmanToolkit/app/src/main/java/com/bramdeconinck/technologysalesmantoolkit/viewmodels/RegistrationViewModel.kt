package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.createProfileUpdates
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isPasswordValid
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.passwordsMatch

class RegistrationViewModel : InjectedViewModel() {

    val navigateToLogin = SingleLiveEvent<Any>()

    val showPrivacyPolicyDialog = SingleLiveEvent<Any>()

    fun validateRegistrationForm(firstname: String, familyname: String, email: String, password: String, repeatPassword: String) {
        if (!everyFieldHasValue(listOf(firstname, familyname, email, password, repeatPassword))) {
            makeToast(R.string.error_empty_fields)
            return
        }

        if (!isEmailValid(email)) {
            makeToast(R.string.error_invalid_email)
            return
        }

        if (!isPasswordValid(password)) {
            makeToast(R.string.error_invalid_password)
            return
        }

        if (!passwordsMatch(password, repeatPassword)) {
            makeToast(R.string.error_passwords_dont_match)
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
                                        makeToast(R.string.message_account_created)
                                        navigateToLogin.call()
                                    } else { makeToast(R.string.error_account_not_created) }
                                }
                    } else { makeToast(R.string.error_account_not_created) }
                }
    }

}