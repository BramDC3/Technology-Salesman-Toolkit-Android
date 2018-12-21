package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.databinding.FragmentRegistrationBinding
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToastMaker
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showPrivacyPolicyDialog
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.RegistrationViewModel

class RegistrationFragment : Fragment(), IToastMaker {

    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        registrationViewModel = ViewModelProviders.of(activity!!).get(RegistrationViewModel::class.java)

        val rootView = binding.root
        binding.registrationViewModel = registrationViewModel
        binding.setLifecycleOwner(activity)

        registrationViewModel.goToLoginClicked.observe(this, Observer { findNavController().popBackStack() })

        registrationViewModel.showPrivacyPolicyDialog.observe(this, Observer { callPrivacyPolicyDialog() })

        registrationViewModel.registrationErrorOccured.observe(this, Observer { showToast(it!!)  })

        registrationViewModel.firebaseAccountCreation.observe(this, Observer {
            when(it) {
                is BaseCommand.Success -> showToast(it.message!!)
                is BaseCommand.Error -> showToast(it.error!!)
            }
        })

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()

        registrationViewModel.clearRegistrationForm()
    }

    private fun callPrivacyPolicyDialog() {
        showPrivacyPolicyDialog(context!!,
                getString(R.string.title_privacy_policy_dialog),
                getString(R.string.message_privacy_policy_dialog),
                registrationViewModel.createFirebaseAccount()
        )
    }

    override fun showToast(message: Int) { makeToast(context!!, message) }

}
