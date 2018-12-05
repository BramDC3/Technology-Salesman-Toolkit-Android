package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.*
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showPrivacyPolicyDialog
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_registration_register.setOnClickListener { validateRegistrationForm() }

        tv_registration_backToLogin.setOnClickListener { it.findNavController().popBackStack() }
    }

    private fun createFirebaseAccount(): () -> Unit = {
        firebaseAuth.createUserWithEmailAndPassword(et_registration_email.text.toString(), et_registration_password.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateUserInfo()
                    } else {
                        MessageUtils.makeToast(context!!, getString(R.string.error_account_not_created))
                        btn_registration_register.isEnabled = true
                    }
                }
    }

    private fun updateUserInfo() {
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("${et_registration_firstname.text} ${et_registration_lastname.text}")
                .build()

        firebaseUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser!!.sendEmailVerification()
                        firebaseAuth.signOut()
                        MessageUtils.makeToast(context!!, getString(R.string.message_account_created))
                        this.findNavController().popBackStack()
                    }
                }
    }

    private fun validateRegistrationForm() {
        if (!ValidationUtils.everyFieldHasValue(listOf(et_registration_firstname.text.toString(), et_registration_lastname.text.toString(), et_registration_email.text.toString(), et_registration_password.text.toString(), et_registration_repeatPassword.text.toString()))) {
            MessageUtils.makeToast(context!!, getString(R.string.error_empty_fields))
            return
        }

        if (!ValidationUtils.isEmailValid(et_registration_email.text.toString())) {
            MessageUtils.makeToast(context!!, getString(R.string.error_invalid_email))
            return
        }

        if (!ValidationUtils.isPasswordValid(et_registration_password.text.toString())) {
            MessageUtils.makeToast(context!!, getString(R.string.error_invalid_password))
            return
        }

        if (!ValidationUtils.passwordsMatch(et_registration_password.text.toString(), et_registration_repeatPassword.text.toString())) {
            MessageUtils.makeToast(context!!, getString(R.string.error_passwords_dont_match))
            return
        }

        showPrivacyPolicyDialog(context!!, "Privacybeleid", "Door op 'Ja' te drukken, gaat u akkoord met het privacybeleid en zal uw account aangemaakt worden. Er zal een bevestigingsmail naar uw e-mailadres verzonden worden.", createFirebaseAccount())
    }

}