package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.databinding.FragmentLoginBinding
import com.bramdeconinck.technologysalesmantoolkit.interfaces.ToastMaker
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

/**
 * [LoginFragment] is a [Fragment] where users can sign in.
 */
class LoginFragment : Fragment(), ToastMaker {

    /**
     * [loginViewModel] contains all data and functions that have to do with logging in.
     */
    private lateinit var loginViewModel: LoginViewModel

    /**
     * [binding] is used for data binding.
     */
    private lateinit var binding: FragmentLoginBinding

    /**
     * [gso] are the [GoogleSignInOptions] used to sign in with a Google account.
     */
    private lateinit var gso: GoogleSignInOptions

    /**
     * [googleSignInClient] is the screen users can see to sign in with their Google account.
     */
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        loginViewModel =  ViewModelProviders.of(activity!!).get(LoginViewModel::class.java)

        val rootView = binding.root
        binding.loginViewModel = loginViewModel
        binding.setLifecycleOwner(activity)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(context!!, gso)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        subscribeToObservables()
    }

    /**
     * Function to subscribe to the observables of the [LoginViewModel].
     */
    private fun subscribeToObservables() {
        loginViewModel.navigateToServiceList.observe(this, Observer { goToServiceList()})

        loginViewModel.signInWithGoogleClicked.observe(this, Observer { signInWithGoogle() })

        loginViewModel.goToRegistrationClicked.observe(this, Observer { goToRegistration() })

        loginViewModel.loginErrorOccurred.observe(this, Observer { showToast(it!!) })
    }

    /**
     * Function to open a screen for signing in with a Google account.
     */
    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    /**
     * Function that handles the result of the intent of the function above.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                loginViewModel.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) { print(e.message) }
        }
    }

    /**
     * Function for navigating to the [ServiceListFragment] and welcoming the user.
     */
    private fun goToServiceList() {
        makeToast(context!!, R.string.message_welcome, firebaseUser!!.displayName)
        findNavController().navigate(R.id.toServiceList)
    }

    /**
     * Function for navigating to the [RegistrationFragment].
     */
    private fun goToRegistration() { findNavController().navigate(R.id.toRegistration) }

    /**
     * Function for showing a toast message.
     *
     * @param [message]: String resource Id.
     */
    override fun showToast(message: Int) { makeToast(context!!, message) }

}