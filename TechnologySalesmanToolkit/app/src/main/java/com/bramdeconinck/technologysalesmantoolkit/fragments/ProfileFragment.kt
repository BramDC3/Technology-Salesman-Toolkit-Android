package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showEditProfileDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showThreeButtonsPositiveFuncDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var menuItem: MenuItem
    private var editable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lbl_profile_change_password.setOnClickListener { showThreeButtonsPositiveFuncDialog(context!!, "Wachtwoord wijzigen", "Bent u zeker dat u uw wachtwoord wilt wijzigen? Als u op 'Ja' drukt, zal er een e-mail verzonden worden waarmee uw wachtwoord opnieuw ingesteld kan worden.", sendResetPasswordEmail())  }

        img_profile_image.setOnClickListener{ MessageUtils.showBasicDialog(context!!, "Profielfoto wijzigen", "Momenteel kan u enkel uw profielfoto wijzigen door aan te melden met uw Google account. ")  }

        btn_profile_edit_profile.setOnClickListener { validateProfileForm() }
    }

    override fun onStart() {
        super.onStart()

        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_profile, menu)
        menuItem = menu!!.findItem(R.id.action_edit_profile)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return (when(item?.itemId) {
            R.id.action_edit_profile -> {
                toggleEditMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }

    // Function to update the UI with data of the current FirebaseUser
    private fun updateUI() {
        Glide.with(this)
                .load(firebaseUser!!.photoUrl ?: R.drawable.default_profile_image)
                .apply(RequestOptions.circleCropTransform())
                .into(img_profile_image)

        lbl_profile_fullname.text = firebaseUser!!.displayName
        txt_profile_firstname.setText(StringUtils.getFirstName(firebaseUser!!.displayName!!))
        txt_profile_familyname.setText(StringUtils.getFamilyName(firebaseUser!!.displayName!!))
        txt_profile_email.setText(firebaseUser!!.email)
    }

    // Function to send an e-mail to the current FirebaseUser containing a link to change their password
    private fun sendResetPasswordEmail(): () -> Unit = {
        firebaseAuth.sendPasswordResetEmail(firebaseUser!!.email!!)
                .addOnSuccessListener { MessageUtils.makeToast(context!!, "Er werd een e-mail naar u verzonden waarmee u uw wachtwoord kunt wijzigen.") }
                .addOnFailureListener { MessageUtils.makeToast(context!!, "Er is een fout opgetreden tijdens het proberen versturen van de e-mail.") }
    }

    // Function to go from normal mode to editing mode and vice versa
    private fun toggleEditMode() {
        editable = !editable

        txt_profile_email.isEnabled = editable
        txt_profile_firstname.isEnabled = editable
        txt_profile_familyname.isEnabled = editable

        if (editable) enterProfileEditMode() else exitProfileEditMode()
    }

    // Changes for the UI on entering the edit mode
    private fun enterProfileEditMode() {
        btn_profile_edit_profile.visibility = View.VISIBLE
        lbl_profile_change_password.visibility = View.GONE
        menuItem.icon = context!!.getDrawable(R.drawable.ic_close_black_24dp)
        menuItem.title = getString(R.string.title_action_stop_editing_profile)
    }

    // Changes for the UI on exiting the edit mode
    private fun exitProfileEditMode() {
        btn_profile_edit_profile.visibility = View.GONE
        lbl_profile_change_password.visibility = View.VISIBLE
        menuItem.icon = context!!.getDrawable(R.drawable.ic_edit_black_24dp)
        menuItem.title = getString(R.string.title_action_edit_profile)

        updateUI()
    }

    private fun validateProfileForm() {
        btn_profile_edit_profile.isEnabled = false

        if (!ValidationUtils.atLeastOneFieldChanged(mapOf(txt_profile_email.text.toString() to firebaseUser!!.email!!,
                        txt_profile_firstname.text.toString() to StringUtils.getFirstName(firebaseUser!!.displayName!!),
                        txt_profile_familyname.text.toString() to StringUtils.getFamilyName(firebaseUser!!.displayName!!)))) {
            toggleEditMode()
            btn_profile_edit_profile.isEnabled = true
            return
        }

        if (!ValidationUtils.everyFieldHasValue(listOf(txt_profile_firstname.text.toString(), txt_profile_familyname.text.toString(), txt_profile_email.text.toString()))) {
            MessageUtils.makeToast(context!!, getString(R.string.error_empty_fields))
            btn_profile_edit_profile.isEnabled = true
            return
        }

        if (!ValidationUtils.isEmailValid(txt_profile_email.text.toString())) {
            MessageUtils.makeToast(context!!, getString(R.string.error_invalid_email))
            btn_profile_edit_profile.isEnabled = true
            return
        }

        showEditProfileDialog(context!!, "Profiel wijzigen", "Bent u zeker dat u uw profiel wilt wijzigen?", applyProfileChanges(), enableProfileEditButton())
    }

    private fun enableProfileEditButton() = { btn_profile_edit_profile.isEnabled = true }

    private fun applyProfileChanges() =  {
        if (firebaseUser!!.displayName != "${txt_profile_firstname.text} ${txt_profile_familyname.text}") {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("${txt_profile_firstname.text} ${txt_profile_familyname.text}")
                    .build()

            firebaseUser!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            MessageUtils.showBasicDialog(context!!, "Naam wijzigen", "Uw naam werd succesvol gewijzigd")
                            updateUI()
                            if (editable) toggleEditMode()
                        }
                    }
                    .addOnFailureListener {
                        MessageUtils.showBasicDialog(context!!, "Naam wijzigen", "Er is een fout opgetreden tijdens het wijzigen van uw naam.")
                    }
        }

        if (firebaseUser!!.email != txt_profile_email.text.toString()) {
            firebaseUser!!.updateEmail(txt_profile_email.text.toString())
                    .addOnCompleteListener { task1 ->
                        if (task1.isSuccessful) {
                            firebaseUser!!.sendEmailVerification()
                                    .addOnCompleteListener { task2 ->
                                        if (task2.isSuccessful) {
                                            MessageUtils.showBasicDialog(context!!, "E-mailadres wijzigen", "Uw e-mailadres werd succesvol gewijzigd.")
                                            firebaseAuth.signOut()
                                            this.findNavController().navigate(R.id.signOutFromProfile)
                                        }
                                    }
                        }
                    }
                    .addOnFailureListener {
                        MessageUtils.showBasicDialog(context!!, "E-mailadres wijzigen", "Er is een fout opgetreden tijdens het wijzigen van uw e-mailadres.")
                    }
        }

        btn_profile_edit_profile.isEnabled = true
    }

}
