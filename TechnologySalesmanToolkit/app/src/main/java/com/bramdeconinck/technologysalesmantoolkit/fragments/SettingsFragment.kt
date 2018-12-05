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
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.privacyPolicy
import com.bramdeconinck.technologysalesmantoolkit.utils.website
import kotlinx.android.synthetic.main.fragment_settings.*
import android.widget.FrameLayout
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.WebpageUtils.openWebPage


class SettingsFragment : Fragment(), IFirebaseSuggestionCallback {

    private lateinit var firestoreApi: FirestoreAPI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_settings, container, false)

        firestoreApi = FirestoreAPI()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_settings_website.setOnClickListener{ openWebPage(website, context!!) }

        btn_settings_darkmode.setOnClickListener{ switch_settings_darkmode.isChecked = !switch_settings_darkmode.isChecked }

        btn_settings_privacypolicy.setOnClickListener { openWebPage(privacyPolicy, context!!) }

        btn_settings_suggestion.setOnClickListener{ showMakeSuggestionDialog(context!!, "Verstuur een suggestie","Vul hieronder uw suggestie in en druk op de knop 'Verzend'.") }

        btn_settings_signout.setOnClickListener{ showSignOutDialog(context!!, "Afmelden", "Bent u zeker dat u zich wilt afmelden?") }
    }

    private fun showSignOutDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ ->
                    firebaseAuth.signOut()
                    this.findNavController().navigate(R.id.signOutFromSettings)
                }
                .setNegativeButton("Nee") { dialog, _ -> dialog.dismiss() }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    private fun showMakeSuggestionDialog(context: Context, title: String, message: String) {
        val editText = EditText(context)
        editText.setSingleLine(false)
        val container = FrameLayout(context)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.marginStart = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        params.marginEnd = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        editText.layoutParams = params
        container.addView(editText)

        AlertDialog.Builder(context)
                .setTitle(title)
                .setView(container)
                .setMessage(message)
                .setPositiveButton("Verzend") { _, _ -> validateSuggestion(editText.text.toString()) }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    private fun validateSuggestion(suggestion: String) {
        if (ValidationUtils.everyFieldHasValue(listOf(suggestion))) firestoreApi.postSuggestion(this, suggestion)
        else MessageUtils.makeToast(context!!, "Een lege suggestie kan niet worden verstuurd.")
    }

    override fun showSuccesMessage() { MessageUtils.makeToast(context!!, "Bedankt voor het verzenden van uw suggestie!") }

    override fun showFailureMessage() { MessageUtils.makeToast(context!!, "Er is iets fout gegaan tijdens het versturen van uw suggestie.") }

}