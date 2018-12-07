package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.utils.privacyPolicy
import com.bramdeconinck.technologysalesmantoolkit.utils.website
import kotlinx.android.synthetic.main.fragment_settings.*
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showMakeSuggestionDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showThreeButtonsPositiveFuncDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.WebpageUtils.openWebPage
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsViewModel = ViewModelProviders.of(activity!!).get(SettingsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_settings_website.setOnClickListener{ openWebPage(website) }

        btn_settings_darkmode.setOnClickListener{ switch_settings_darkmode.isChecked = !switch_settings_darkmode.isChecked }

        btn_settings_privacypolicy.setOnClickListener { openWebPage(privacyPolicy) }

        btn_settings_suggestion.setOnClickListener{ showMakeSuggestionDialog(
                getString(R.string.title_send_suggestion),
                getString(R.string.message_send_suggestion),
                settingsViewModel.getSuggestion()
            )
        }

        btn_settings_signout.setOnClickListener{ showThreeButtonsPositiveFuncDialog(
                getString(R.string.title_sign_out),
                getString(R.string.message_sign_out),
                signOut()
            )
        }
    }

    private fun signOut() = {
        firebaseAuth.signOut()
        this.findNavController().navigate(R.id.signOutFromSettings)
    }

}