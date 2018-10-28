package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mAuth = FirebaseAuth.getInstance()

        mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lbl_goToRegistration.setOnClickListener {
            it.findNavController().navigate(R.id.toRegistration)
        }

        btn_signIn.setOnClickListener { _: View? -> validateLoginForm() }
        btn_signInWithGoogle.setOnClickListener { _: View? -> signInWithGoogle() }
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
                print(e.message)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mAuth.currentUser
                        Utils.makeToast(this.requireContext(), getString(R.string.welcome, firebaseUser?.displayName))
                        this.findNavController().navigate(R.id.toServiceList)
                    } else {
                        Utils.makeToast(this.requireContext(), getString(R.string.sign_in_error))
                    }
                }
    }

    private fun logInWithFirebaseAccount() {
        mAuth.signInWithEmailAndPassword(txt_email.text.toString(), txt_password.text.toString())
                .addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        if (mAuth.currentUser!!.isEmailVerified) {
                            val firebaseUser = mAuth.currentUser
                            Utils.makeToast(this.requireContext(), getString(R.string.welcome, firebaseUser?.displayName))
                            this.findNavController().navigate(R.id.toServiceList)
                        } else {
                            Utils.makeToast(this.requireContext(), getString(R.string.email_is_not_verified))
                        }
                    } else {
                        Utils.makeToast(this.requireContext(), getString(R.string.sign_in_error))
                    }
                }
    }

    private fun validateLoginForm() {
        if (!txt_email.text.isBlank()
                && !txt_password.text.isBlank()) {
            if (Utils.isValid(txt_email.text.toString())) {
                logInWithFirebaseAccount()
            } else {
                Utils.makeToast(this.requireContext(), getString(R.string.invalid_email))
            }
        } else {
            Utils.makeToast(this.requireContext(), getString(R.string.empty_field))
        }
    }

}