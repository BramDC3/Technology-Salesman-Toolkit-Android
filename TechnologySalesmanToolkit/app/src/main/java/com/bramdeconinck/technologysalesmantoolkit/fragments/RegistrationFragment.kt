package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_register.setOnClickListener { validateRegistrationForm() }

        lbl_backToLogin.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun createFirebaseAccount() {
        mAuth.createUserWithEmailAndPassword(txt_email_r.text.toString(), txt_password_r.text.toString())
                .addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mAuth.currentUser
                        updateUserInfo(firebaseUser)
                    } else {
                        MessageUtils.makeToast(this.requireContext(), getString(R.string.account_not_created))
                    }
                }
    }

    private fun updateUserInfo(firebaseUser: FirebaseUser?) {
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("${txt_firstname.text} ${txt_lastname.text}")
                .build()

        firebaseUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser.sendEmailVerification()
                        mAuth.signOut()
                        MessageUtils.makeToast(this.requireContext(), getString(R.string.account_created))
                        this.findNavController().popBackStack()
                    }
                }
    }

    private fun validateRegistrationForm() {
        if (!txt_firstname.text.isBlank()
                && !txt_lastname.text.isBlank()
                && !txt_email_r.text.isBlank()
                && !txt_password_r.text.isBlank()
                && !txt_repeatpassword.text.isBlank()) {
            if (ValidationUtils.isEmailValid(txt_email_r.text.toString())) {
                if (txt_password_r.text.toString() == txt_repeatpassword.text.toString()) {
                    showPrivacyPolicyDialog(context!!, "Privacybeleid", "Door op 'Ja' te drukken, gaat u akkoord met het privacybeleid en wordt uw account aangemaakt.")
                } else {
                    MessageUtils.makeToast(this.requireContext(), getString(R.string.passwords_dont_match))
                }
            } else {
                MessageUtils.makeToast(this.requireContext(), getString(R.string.invalid_email))
            }
        } else {
            MessageUtils.makeToast(this.requireContext(), getString(R.string.empty_field))
        }
    }

    private fun showPrivacyPolicyDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> createFirebaseAccount() }
                .setNegativeButton("Nee") { dialog, _ -> dialog.dismiss() }
                .setNeutralButton("Bekijk privacybeleid") { _, _ -> openWebPage(privacyPolicy) }
                .create()
                .show()
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

}