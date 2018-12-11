package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToastMaker
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showPrivacyPolicyDialog
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.RegistrationViewModel
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment(), IToastMaker {

    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registrationViewModel = ViewModelProviders.of(activity!!).get(RegistrationViewModel::class.java)

        registrationViewModel.navigateToLogin.observe(this, Observer { navigateBackToLogin() })

        registrationViewModel.showPrivacyPolicyDialog.observe(this, Observer { callPrivacyPolicyDialog() })

        registrationViewModel.registrationFormValidation.observe(this, Observer {
            when(it) {
                is BaseCommand.Error -> showToast(it.error!!)
            }
        })

        registrationViewModel.firebaseAccountCreation.observe(this, Observer {
            when(it) {
                is BaseCommand.Success -> showToast(it.message!!)
                is BaseCommand.Error -> showToast(it.error!!)
            }
        })

        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_registration_register.setOnClickListener {
            registrationViewModel.isRegistrationFormValid(
                et_registration_firstname.text.toString(),
                et_registration_lastname.text.toString(),
                et_registration_email.text.toString(),
                et_registration_password.text.toString(),
                et_registration_repeatPassword.text.toString()
            )
        }

        tv_registration_backToLogin.setOnClickListener { navigateBackToLogin() }
    }

    private fun callPrivacyPolicyDialog() {
        showPrivacyPolicyDialog(context!!,
                getString(R.string.title_privacy_policy_dialog),
                getString(R.string.message_privacy_policy_dialog),
                registrationViewModel.createFirebaseAccount(
                        et_registration_firstname.text.toString(),
                        et_registration_lastname.text.toString(),
                        et_registration_email.text.toString(),
                        et_registration_password.text.toString()))
    }

    private fun navigateBackToLogin() = this.findNavController().popBackStack()

    override fun showToast(message: Int) { makeToast(context!!, message) }

}
