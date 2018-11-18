package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.google.firebase.auth.FirebaseAuth
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils.showDialog
import com.bumptech.glide.util.Util
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
        btn_settings_darkmode.setOnClickListener{
            Utils.makeSnackBar(this.activity!!.findViewById(android.R.id.content), "Donkere modus is momenteel nog niet beschikbaar.")
        }
        btn_settings_privacypolicy.setOnClickListener{
            showDialog(this.requireContext(),"Privacybeleid", "Er is momenteel nog geen privacy beleid.")
        }
        btn_settings_suggestion.setOnClickListener{
            Utils.showDialog(this.requireContext(), "Verstuur een suggestie","Suggesties versturen kan momenteel nog niet.")
        }
        btn_settings_signout.setOnClickListener{
            mAuth.signOut()
            this.findNavController().navigate(R.id.signOut)
        }
    }

    private fun openWebPage(url: String) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Utils.makeToast(this.requireContext(), "No application can handle this request. Please install a web browser or check your URL.")
            e.printStackTrace()
        }
    }

    companion object {
        private const val website: String = "https://bramdeconinck.com"
    }

}
