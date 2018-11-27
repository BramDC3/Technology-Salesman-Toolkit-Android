package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.google.firebase.auth.FirebaseAuth
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.privacyPolicy
import com.bramdeconinck.technologysalesmantoolkit.utils.website
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_settings, container, false)

        mAuth = FirebaseAuth.getInstance()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_settings_website.setOnClickListener{ openWebPage(website) }

        btn_settings_darkmode.setOnClickListener{ switch_settings_darkmode.isChecked = !switch_settings_darkmode.isChecked }

        btn_settings_privacypolicy.setOnClickListener { openWebPage(privacyPolicy) }

        btn_settings_suggestion.setOnClickListener{
            MessageUtils.showDialog(context!!, "Verstuur een suggestie","Suggesties versturen kan momenteel nog niet.")
        }

        btn_settings_signout.setOnClickListener{
            showSignOutDialog(context!!, "Afmelden", "Bent u zeker dat u zich wilt afmelden?")
        }
    }

    private fun openWebPage(url: String) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            MessageUtils.makeToast(this.requireContext(), "Er werd geen webbrowser gedetecteerd op uw toestel.")
            e.printStackTrace()
        }
    }

    private fun showSignOutDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ ->
                    mAuth.signOut()
                    this.findNavController().navigate(R.id.signOutFromSettings)
                }
                .setNegativeButton("Nee") { dialog, _ -> dialog.dismiss() }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

}
