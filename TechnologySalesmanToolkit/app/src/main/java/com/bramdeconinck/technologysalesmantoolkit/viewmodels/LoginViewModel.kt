package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.Event
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class LoginViewModel : InjectedViewModel() {

    @Inject
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    private val _navigateToDetails = MutableLiveData<Event<String>>()

    val navigateToDetails : LiveData<Event<String>>
        get() = _navigateToDetails

    fun validateLoginForm(email: String, password: String) {
        //btn_login_signIn.isEnabled = false

        if (!everyFieldHasValue(listOf(email, password))) {
            makeToast(context, context.getString(R.string.error_empty_fields))
            //btn_login_signIn.isEnabled = true
            return
        }

        if (!isEmailValid(email)) {
            makeToast(context, context.getString(R.string.error_invalid_email))
            //btn_login_signIn.isEnabled = true
            return
        }

        logInWithFirebaseAccount(email, password)
    }

    private fun logInWithFirebaseAccount(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser!!.isEmailVerified) {
                            makeToast(context, context.getString(R.string.message_welcome, firebaseUser!!.displayName))
                            _navigateToDetails.value = Event("")
                        } else {
                            firebaseAuth.signOut()
                            makeToast(context, context.getString(R.string.error_email_is_not_verified))
                            //btn_login_signIn.isEnabled = true
                        }
                    } else {
                        makeToast(context, context.getString(R.string.sign_in_error))
                        //btn_login_signIn.isEnabled = true
                    }
                }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = firebaseAuth.currentUser
                        makeToast(context, context.getString(R.string.message_welcome, firebaseUser!!.displayName))
                        _navigateToDetails.value = Event("")
                    } else {
                        makeToast(context, context.getString(R.string.sign_in_error))
                    }
                }
    }
}