package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel : InjectedViewModel() {

    val navigateToServiceList = SingleLiveEvent<Any>()

    val emailIsNotVerified = SingleLiveEvent<Any>()

    val signInErrorOccurred = SingleLiveEvent<Any>()

    val loginFormValidation = SingleLiveEvent<BaseCommand>()

    fun logInWithFirebaseAccount(email: String, password: String) {
        if (!isLoginFormValid(email, password)) return
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser!!.isEmailVerified) {
                            navigateToServiceList.call()
                        } else {
                            firebaseAuth.signOut()
                            emailIsNotVerified.call()
                        }
                    } else { signInErrorOccurred.call() }
                }
    }

    private fun isLoginFormValid(email: String, password: String): Boolean {
        if (!everyFieldHasValue(listOf(email, password))) {
            loginFormValidation.value = BaseCommand.Error(R.string.error_empty_fields)
            return false
        }

        if (!isEmailValid(email)) {
            loginFormValidation.value = BaseCommand.Error(R.string.error_invalid_email)
            return false
        }

        return true
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        navigateToServiceList.call()
                    } else { signInErrorOccurred.call() }
                }
    }
}