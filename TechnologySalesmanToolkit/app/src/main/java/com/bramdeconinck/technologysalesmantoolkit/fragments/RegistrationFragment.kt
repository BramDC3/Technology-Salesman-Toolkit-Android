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
import com.bramdeconinck.technologysalesmantoolkit.interfaces.ToastMaker
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showPrivacyPolicyDialog
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.LoginViewModel
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.RegistrationViewModel

/**
 * [RegistrationFragment] is a [Fragment] where users can create an account.
 */
class RegistrationFragment : Fragment(), ToastMaker {

    /**
     * [registrationViewModel] contains all data and functions that have to do with creating an account.
     */
    private lateinit var registrationViewModel: RegistrationViewModel

    /**
     * [binding] is used for data binding.
     */
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        registrationViewModel = ViewModelProviders.of(activity!!).get(RegistrationViewModel::class.java)

        val rootView = binding.root
        binding.registrationViewModel = registrationViewModel
        binding.setLifecycleOwner(activity)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        subscribeToObservables()
    }

    /**
     * Function to subscribe to the observables of the [RegistrationViewModel].
     */
    private fun subscribeToObservables() {
        registrationViewModel.goToLoginClicked.observe(this, Observer { findNavController().popBackStack() })

        registrationViewModel.showPrivacyPolicyDialog.observe(this, Observer { showPrivacyPolicyDialog() })

        registrationViewModel.registrationEvent.observe(this, Observer { showToast(it!!)  })
    }

    /**
     * Function for displaying a dialog where users can read the privacy policy and create their account.
     */
    private fun showPrivacyPolicyDialog() {
        showPrivacyPolicyDialog(
                context!!,
                getString(R.string.title_privacy_policy_dialog),
                getString(R.string.message_privacy_policy_dialog),
                registrationViewModel.createFirebaseAccount()
        )
    }

    /**
     * Function for showing a toast message.
     *
     * @param [message]: String resource Id.
     */
    override fun showToast(message: Int) { makeToast(context!!, message) }

}