package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToastMaker
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.GOOGLE_SIGN_IN_REQUEST_CODE
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), IToastMaker {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loginViewModel =  ViewModelProviders.of(activity!!).get(LoginViewModel::class.java)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)

        loginViewModel.emailIsNotVerified.observe(this, Observer { showToast(R.string.error_email_is_not_verified) })

        loginViewModel.signInErrorOccurred.observe(this, Observer { showToast(R.string.sign_in_error) })

        loginViewModel.loginFormValidation.observe(this, Observer {
            when (it) {
                is BaseCommand.Error -> showToast(it.error!!)
            }
        })

        loginViewModel.navigateToServiceList.observe(this, Observer {
            makeToast(context!!, R.string.message_welcome, firebaseUser!!.displayName)
            this.findNavController().navigate(R.id.toServiceList)
        })

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login_signIn.setOnClickListener {
            loginViewModel.logInWithFirebaseAccount(
                    et_login_email.text.toString(),
                    et_login_password.text.toString()
            )
        }

        btn_login_signInWithGoogle.setOnClickListener { signInWithGoogle() }

        tv_login_goToRegistration.setOnClickListener { it.findNavController().navigate(R.id.toRegistration) }
    }

    private fun signInWithGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                loginViewModel.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                print(e.message)
            }
        }
    }

    override fun showToast(message: Int) {makeToast(context!!, message)}

}
