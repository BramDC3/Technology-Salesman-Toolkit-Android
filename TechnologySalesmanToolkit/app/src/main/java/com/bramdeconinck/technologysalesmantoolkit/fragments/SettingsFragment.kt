package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.databinding.FragmentSettingsBinding
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.interfaces.ToastMaker
import com.bramdeconinck.technologysalesmantoolkit.utils.PRIVACY_POLICY
import com.bramdeconinck.technologysalesmantoolkit.utils.TEST_WEBSITE
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showMakeSuggestionDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showThreeButtonsPositiveFunctionDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.WebpageUtils.openWebPage
import com.bramdeconinck.technologysalesmantoolkit.utils.SHARED_PREFERENCES_KEY_THEME
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.SettingsViewModel

/**
 * [SettingsFragment] is a [Fragment] where users can find information about the app, sign out and change the theme of the app.
 */
class SettingsFragment : Fragment(), ToastMaker {

    /**
     * [settingsViewModel] contains all data and functions that have to do with the settings.
     */
    private lateinit var settingsViewModel: SettingsViewModel

    /**
     * [binding] is used for data binding.
     */
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        settingsViewModel = ViewModelProviders.of(activity!!).get(SettingsViewModel::class.java)

        val rootView = binding.root
        binding.settingsViewModel = settingsViewModel
        binding.setLifecycleOwner(activity)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        subscribeToObservables()
    }

    /**
     * Function to subscribe to the observables of the [SettingsViewModel].
     */
    private fun subscribeToObservables() {
        settingsViewModel.visitWebsiteClicked.observe(this, Observer { openWebPage(context!!, TEST_WEBSITE) })

        settingsViewModel.visitPrivacyPolicyClicked.observe(this, Observer { openWebPage(context!!, PRIVACY_POLICY) })

        settingsViewModel.isDarkModeEnabled.observe(this, Observer { changeSelectedTheme(it!!) })

        settingsViewModel.makeSuggestionClicked.observe(this, Observer { makeSuggestion() })

        settingsViewModel.showSignOutDialogClicked.observe(this, Observer { signOutRequested() })

        settingsViewModel.emptySuggestion.observe(this, Observer { showToast(R.string.send_suggestion_empty_error) })

        settingsViewModel.suggestionCallback.observe(this, Observer {
            when (it) {
                is BaseCommand.Success -> showToast(it.message!!)
                is BaseCommand.Error -> showToast(it.error!!)
            }
        })

        settingsViewModel.signOutTriggered.observe(this, Observer { signOutTriggered() })
    }

    /**
     * Function for displaying a dialog where users can make a suggestion.
     */
    private fun makeSuggestion() {
        showMakeSuggestionDialog(
                context!!,
                getString(R.string.title_send_suggestion),
                getString(R.string.message_send_suggestion),
                settingsViewModel.getSuggestion()
        )
    }

    /**
     * Function for displaying a dialog where users can choose whether they want to sign out or not.
     */
    private fun signOutRequested() {
        showThreeButtonsPositiveFunctionDialog(
                context!!,
                getString(R.string.title_sign_out),
                getString(R.string.message_sign_out),
                settingsViewModel.signOut()
        )
    }

    /**
     * Function called when the user signed out to redirect them to the [LoginFragment].
     */
    private fun signOutTriggered() { findNavController().navigate(R.id.signOutFromSettings) }

    /**
     * Function to change the theme of the app to day or night mode and save this preference.
     *
     * @param [night]: Indicates whether the selected theme should be night theme or day theme.
     */
    private fun changeSelectedTheme(night: Boolean) {
         val theme = if (night) {
            (activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.MODE_NIGHT_YES
        }
        else {
            (activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.MODE_NIGHT_NO
        }

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPref.edit().putInt(SHARED_PREFERENCES_KEY_THEME, theme).apply()
    }

    /**
     * Function for showing a toast message.
     *
     * @param [message]: String resource Id.
     */
    override fun showToast(message: Int) { makeToast(context!!, message) }

}