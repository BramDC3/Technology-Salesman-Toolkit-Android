package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel : InjectedViewModel() {

    val navigateToServiceList = SingleLiveEvent<Any>()

    fun logInWithFirebaseAccount(email: String, password: String) {
        if (!loginFormIsValid(email, password)) return
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser!!.isEmailVerified) {
                            makeToast(R.string.message_welcome, firebaseUser!!.displayName)
                            navigateToServiceList.call()
                        } else {
                            firebaseAuth.signOut()
                            makeToast(R.string.error_email_is_not_verified)
                        }
                    } else { makeToast(R.string.sign_in_error) }
                }
    }

    private fun loginFormIsValid(email: String, password: String): Boolean {
        if (!everyFieldHasValue(listOf(email, password))) {
            makeToast(R.string.error_empty_fields)
            return false
        }

        if (!isEmailValid(email)) {
            makeToast(R.string.error_invalid_email)
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
                        makeToast(R.string.message_welcome, firebaseUser!!.displayName)
                        navigateToServiceList.call()
                    } else { makeToast(R.string.sign_in_error) }
                }
    }
}