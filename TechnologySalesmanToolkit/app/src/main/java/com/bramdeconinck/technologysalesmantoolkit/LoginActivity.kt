package com.bramdeconinck.technologysalesmantoolkit

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

            btn_signInWithGoogle.setOnClickListener { _: View? ->
                signInWithGoogle()
            }

            lbl_goToRegistration.setOnClickListener { _: View? ->
                goToRegistration()
            }
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
                Toast.makeText(this, getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mAuth.currentUser
                        Toast.makeText(this, "Welkom, ${firebaseUser?.displayName}!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun goToRegistration() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

}
