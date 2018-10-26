package com.bramdeconinck.technologysalesmantoolkit.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils.isValid
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils.makeToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mAuth: FirebaseAuth
    private val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val firebaseUser: FirebaseUser? = mAuth.currentUser

        if (firebaseUser != null) {
            //Intent to MainActivity
        } else {
            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            btn_signIn.setOnClickListener { _: View? -> validateLoginForm() }
            btn_signInWithGoogle.setOnClickListener { _: View? -> signInWithGoogle() }
            lbl_goToRegistration.setOnClickListener { _: View? -> goToRegistration() }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                makeToast(this, getString(R.string.sign_in_error))
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mAuth.currentUser
                        makeToast(this, getString(R.string.welcome, firebaseUser?.displayName))
                    } else {
                        makeToast(this, getString(R.string.sign_in_error))
                    }
                }
    }

    private fun logInWithFirebaseAccount() {
        mAuth.signInWithEmailAndPassword(txt_email.text.toString(), txt_password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser!!.isEmailVerified) {
                            val firebaseUser = mAuth.currentUser
                            makeToast(this, getString(R.string.welcome, firebaseUser?.displayName))
                        } else {
                            makeToast(this, getString(R.string.email_is_not_verified))
                        }
                    } else {
                        makeToast(this, getString(R.string.sign_in_error))
                    }
                }
    }

    private fun validateLoginForm() {
        if (!txt_email.text.isBlank()
                && !txt_password.text.isBlank()) {
            if (isValid(txt_email.text.toString())) {
                logInWithFirebaseAccount()
            }
            makeToast(this, getString(R.string.invalid_email))
        } else {
            makeToast(this, getString(R.string.empty_field))
        }
    }

    private fun goToRegistration() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

}
