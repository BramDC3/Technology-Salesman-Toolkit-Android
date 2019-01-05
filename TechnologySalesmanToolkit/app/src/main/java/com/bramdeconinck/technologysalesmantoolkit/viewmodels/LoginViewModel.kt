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
     * [email] is the email address of the account of the user.
     */
    val email = MutableLiveData<String>()

    /**
     * [password] is the password of the account of the user.
     */
    val password = MutableLiveData<String>()

    /**
     * [SingleLiveEvent] that indicates the user successfully signed in.
     */
    val navigateToServiceList = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] that indicates the user wants to sign in with their Google Account.
     */
    val signInWithGoogleClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] that indicates the user wants to go to the Registration Fragment.
     */
    val goToRegistrationClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] that indicates an error occurred when signing in.
     * It has the value of a String resource Id.
     */
    val loginErrorOccurred = SingleLiveEvent<Int>()

    init {
        email.value = ""
        password.value = ""
    }

    /**
     * Function that gets called when the Google Sign In button is clicked.
     */
    fun signInWithGoogle() { signInWithGoogleClicked.call() }

    /**
     * Function that gets called when the Registration TextView is clicked.
     */
    fun goToRegistration() { goToRegistrationClicked.call() }

    /**
     * Function to sign in with an email address and password combination.
     */
    fun logInWithFirebaseAccount() {
        if (!isLoginFormValid()) return
        firebaseAuth.signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser!!.isEmailVerified) {
                            navigateToServiceList.call()
                            email.value = ""
                            password.value = ""
                        } else {
                            firebaseAuth.signOut()
                            loginErrorOccurred.value = R.string.error_email_is_not_verified
                        }
                    } else { loginErrorOccurred.value = R.string.sign_in_error }
                }
    }

    /**
     * Function to validate the login form.
     *
     * @return: [Boolean] indicating whether the login form is valid or not.
     */
    private fun isLoginFormValid(): Boolean {
        if (!everyFieldHasValue(listOf(email.value!!, password.value!!))) {
            loginErrorOccurred.value = R.string.error_empty_fields
            return false
        }

        if (!isEmailValid(email.value!!)) {
            loginErrorOccurred.value = R.string.error_invalid_email
            return false
        }

        return true
    }

    /**
     * Function to sign in with a Google account.
     *
     * @param acct: Google account object that will be used to sign in.
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

}