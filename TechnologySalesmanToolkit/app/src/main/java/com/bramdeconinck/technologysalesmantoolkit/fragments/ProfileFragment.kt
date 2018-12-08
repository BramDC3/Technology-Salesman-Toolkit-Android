package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showBasicDialog
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

        lbl_profile_change_password.setOnClickListener {
            showThreeButtonsPositiveFuncDialog(
                    context!!,
                    getString(R.string.title_change_password),
                    getString(R.string.message_change_password),
                    sendResetPasswordEmail()
            )
        }

        img_profile_image.setOnClickListener{
            showBasicDialog(
                    context!!,
                    getString(R.string.title_change_profile_picture),
                    getString(R.string.message_change_profile_picture)
            )
        }

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
                .addOnSuccessListener { makeToast(context!!, R.string.change_password_succes) }
                .addOnFailureListener { makeToast(context!!, R.string.change_password_failure) }
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
        val profileFormMap = mapOf(txt_profile_email.text.toString() to firebaseUser!!.email!!,
                txt_profile_firstname.text.toString() to StringUtils.getFirstName(firebaseUser!!.displayName!!),
                txt_profile_familyname.text.toString() to StringUtils.getFamilyName(firebaseUser!!.displayName!!))

        if (!ValidationUtils.atLeastOneFieldChanged(profileFormMap)) {
            toggleEditMode()
            return
        }

        val profileFormList = listOf(txt_profile_firstname.text.toString(),
                txt_profile_familyname.text.toString(),
                txt_profile_email.text.toString())

        if (!ValidationUtils.everyFieldHasValue(profileFormList)) {
            makeToast(context!!, R.string.error_empty_fields)
            return
        }

        if (!ValidationUtils.isEmailValid(txt_profile_email.text.toString())) {
            makeToast(context!!,R.string.error_invalid_email)
            return
        }

        showThreeButtonsPositiveFuncDialog(context!!,
                getString(R.string.title_change_profile),
                getString(R.string.message_change_profile),
                applyProfileChanges())
    }

    private fun applyProfileChanges() =  {
        if (firebaseUser!!.displayName != "${txt_profile_firstname.text} ${txt_profile_familyname.text}") {
            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("${txt_profile_firstname.text} ${txt_profile_familyname.text}")
                    .build()

            firebaseUser!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showBasicDialog(context!!, getString(R.string.title_change_name), getString(R.string.message_change_name))
                            updateUI()
                            if (editable) toggleEditMode()
                        }
                    }
                    .addOnFailureListener { showBasicDialog(context!!, getString(R.string.title_change_name), getString(R.string.error_change_name)) }
        }

        if (firebaseUser!!.email != txt_profile_email.text.toString()) {
            firebaseUser!!.updateEmail(txt_profile_email.text.toString())
                    .addOnCompleteListener { task1 ->
                        if (task1.isSuccessful) {
                            firebaseUser!!.sendEmailVerification()
                                    .addOnCompleteListener { task2 ->
                                        if (task2.isSuccessful) {
                                            showBasicDialog(context!!, getString(R.string.title_change_email), getString(R.string.message_change_email))
                                            firebaseAuth.signOut()
                                            this.findNavController().navigate(R.id.signOutFromProfile)
                                        }
                                    }
                        }
                    }
                    .addOnFailureListener { showBasicDialog(context!!,getString(R.string.title_change_email), getString(R.string.error_change_email)) }
        }
    }

}
