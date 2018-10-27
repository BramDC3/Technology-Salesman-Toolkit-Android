package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*

class RegistrationFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_registration, container, false)

        mAuth = FirebaseAuth.getInstance()

        rootView.btn_register.setOnClickListener { _: View? -> validateRegistrationForm() }

        return rootView
    }

    private fun createFirebaseAccount() {
        mAuth.createUserWithEmailAndPassword(txt_email_r.text.toString(), txt_password_r.text.toString())
                .addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mAuth.currentUser
                        updateUserInfo(firebaseUser)
                    } else {
                        Utils.makeToast(this.requireContext(), getString(R.string.account_not_created))
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
                        Utils.makeToast(this.requireContext(), getString(R.string.account_created))
                    }
                }
    }

    private fun validateRegistrationForm() {
        if (!txt_firstname.text.isBlank()
                && !txt_lastname.text.isBlank()
                && !txt_email_r.text.isBlank()
                && !txt_password_r.text.isBlank()
                && !txt_repeatpassword.text.isBlank()) {
            if (Utils.isValid(txt_email_r.text.toString())) {
                if (txt_password_r.text.toString() == txt_repeatpassword.text.toString()) {
                    createFirebaseAccount()
                } else {
                    Utils.makeToast(this.requireContext(), getString(R.string.passwords_dont_match))
                }
            } else {
                Utils.makeToast(this.requireContext(), getString(R.string.invalid_email))
            }
        } else {
            Utils.makeToast(this.requireContext(), getString(R.string.empty_field))
        }
    }

}