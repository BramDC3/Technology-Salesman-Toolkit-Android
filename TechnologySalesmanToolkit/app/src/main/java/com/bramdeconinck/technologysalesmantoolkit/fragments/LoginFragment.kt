package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loginViewModel =  ViewModelProviders.of(activity!!).get(LoginViewModel::class.java)

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login_signIn.setOnClickListener { validateLoginForm() }

        btn_login_signInWithGoogle.setOnClickListener { signInWithGoogle() }

        tv_login_goToRegistration.setOnClickListener {
            it.findNavController().navigate(R.id.toRegistration)
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
                print(e.message)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        MessageUtils.makeToast(context!!, getString(R.string.message_welcome, firebaseUser!!.displayName))
                        this.findNavController().navigate(R.id.toServiceList)
                    } else {
                        MessageUtils.makeToast(context!!, getString(R.string.sign_in_error))
                    }
                }
    }

    private fun logInWithFirebaseAccount() {
        firebaseAuth.signInWithEmailAndPassword(et_login_email.text.toString(), et_login_password.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (firebaseAuth.currentUser!!.isEmailVerified) {
                            MessageUtils.makeToast(context!!, getString(R.string.message_welcome, firebaseUser!!.displayName))
                            this.findNavController().navigate(R.id.toServiceList)
                        } else {
                            firebaseAuth.signOut()
                            MessageUtils.makeToast(context!!, getString(R.string.error_email_is_not_verified))
                            btn_login_signIn.isEnabled = true
                        }
                    } else {
                        MessageUtils.makeToast(context!!, getString(R.string.sign_in_error))
                        btn_login_signIn.isEnabled = true
                    }
                }
    }

    private fun validateLoginForm() {
        btn_login_signIn.isEnabled = false

        if (!ValidationUtils.everyFieldHasValue(listOf(et_login_email.text.toString(), et_login_password.text.toString()))) {
            MessageUtils.makeToast(context!!, getString(R.string.error_empty_fields))
            btn_login_signIn.isEnabled = true
            return
        }

        if (!ValidationUtils.isEmailValid(et_login_email.text.toString())) {
            MessageUtils.makeToast(context!!, getString(R.string.error_invalid_email))
            btn_login_signIn.isEnabled = true
            return
        }

        logInWithFirebaseAccount()
    }

}