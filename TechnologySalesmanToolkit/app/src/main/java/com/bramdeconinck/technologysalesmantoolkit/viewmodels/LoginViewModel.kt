package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Instance of [InjectedViewModel] that contains data and functions used in the LoginFragment.
 */
class LoginViewModel : InjectedViewModel() {

    /**
     * Values for fields of the login form.
     */
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    /**
     * [SingleLiveEvent] objects used to emit events.
     */
    val navigateToServiceList = SingleLiveEvent<Any>()
    val signInWithGoogleClicked = SingleLiveEvent<Any>()
    val goToRegistrationClicked = SingleLiveEvent<Any>()
    val loginErrorOccurred = SingleLiveEvent<Int>()

    init {
        email.value = ""
        password.value = ""
    }

    fun signInWithGoogle() { signInWithGoogleClicked.call() }

    fun goToRegistration() { goToRegistrationClicked.call() }

    /**
     * Function to sign in with a email address and password combination.
     */
    fun logInWithFirebaseAccount() {
        if (!isLoginFormValid(email.value!!, password.value!!)) return
        firebaseAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser!!.isEmailVerified) {
                            navigateToServiceList.call()
                        } else {
                            firebaseAuth.signOut()
                            loginErrorOccurred.value = R.string.error_email_is_not_verified
                        }
                    } else { loginErrorOccurred.value = R.string.sign_in_error }
                }
    }

    /**
     * Function to validate the login form.
     */
    private fun isLoginFormValid(email: String, password: String): Boolean {
        if (!everyFieldHasValue(listOf(email, password))) {
            loginErrorOccurred.value = R.string.error_empty_fields
            return false
        }

        if (!isEmailValid(email)) {
            loginErrorOccurred.value = R.string.error_invalid_email
            return false
        }

        return true
    }

    /**
     * Function to sign in with a Google account.
     */
    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        navigateToServiceList.call()
                    } else { loginErrorOccurred.value = R.string.sign_in_error }
                }
    }

    fun clearLoginForm() {
        email.value = ""
        password.value = ""
    }

}