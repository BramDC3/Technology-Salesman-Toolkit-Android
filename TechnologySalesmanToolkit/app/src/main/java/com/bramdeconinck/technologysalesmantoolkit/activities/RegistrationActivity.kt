package com.bramdeconinck.technologysalesmantoolkit.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils.isValid
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils.makeToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener { _: View? -> validateRegistrationForm() }
    }

    private fun createFirebaseAccount() {
        mAuth.createUserWithEmailAndPassword(txt_email_r.text.toString(), txt_password_r.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = mAuth.currentUser
                        updateUserInfo(firebaseUser)
                    } else {
                        makeToast(this, getString(R.string.account_not_created))
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
                        makeToast(this, getString(R.string.account_created))
                    }
                }
    }

    private fun validateRegistrationForm() {
        if (!txt_firstname.text.isBlank()
                && !txt_lastname.text.isBlank()
                && !txt_email_r.text.isBlank()
                && !txt_password_r.text.isBlank()
                && !txt_repeatpassword.text.isBlank()) {
            if (isValid(txt_email_r.text.toString())) {
                if (txt_password_r.text.toString() == txt_repeatpassword.text.toString()) {
                    createFirebaseAccount()
                } else {
                    makeToast(this, getString(R.string.passwords_dont_match))
                }
            } else {
                makeToast(this, getString(R.string.invalid_email))
            }
        } else {
            makeToast(this, getString(R.string.empty_field))
        }
    }

}
