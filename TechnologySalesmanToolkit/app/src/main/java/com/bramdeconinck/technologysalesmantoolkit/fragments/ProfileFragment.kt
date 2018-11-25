package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var menuItem: MenuItem
    private var editable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth.currentUser!!
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lbl_profile_change_password.setOnClickListener {
            showChangePasswordDialog(context!!, "Wachtwoord wijzigen", "Bent u zeker dat u uw wachtwoord wilt wijzigen? Als u op 'Ja' drukt, zal er een e-mail verzonden worden waarmee uw wachtwoord opnieuw ingesteld kan worden.")
        }

        img_profile_image.setOnClickListener{
            if (editable) showChangeProfilePictureDialog(context!!, "Profielfoto wijzigen", "Wilt u een nieuwe foto maken met de camera of wilt u een foto uit uw gallerij gebruiken als nieuwe profielfoto?")
        }

        btn_profile_edit_profile.setOnClickListener {
            toggleEditMode()
        }
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
                .load(firebaseUser.photoUrl ?: R.drawable.default_profile_image)
                .apply(RequestOptions.circleCropTransform())
                .into(img_profile_image)

        lbl_profile_fullname.text = firebaseUser.displayName
        txt_profile_firstname.setText(StringUtils.getFirstName(firebaseUser.displayName!!))
        txt_profile_familyname.setText(StringUtils.getFamilyName(firebaseUser.displayName!!))
        txt_profile_email.setText(firebaseUser.email)
    }

    // Function to show the dialog which asks the user if they want to change their password
    private fun showChangePasswordDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> sendResetPasswordEmail()}
                .setNegativeButton("Nee") { dialog, _ -> dialog?.dismiss() }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog?.dismiss() }
                .create()
                .show()
    }

    // Function to send an e-mail to the current FirebaseUser containing a link to change their password
    private fun sendResetPasswordEmail() {
        mAuth.sendPasswordResetEmail(firebaseUser.email!!)
                .addOnSuccessListener {
                    MessageUtils.makeToast(context!!, "Er werd een e-mail naar u verzonden waarmee u uw wachtwoord kunt wijzigen.")
                }
                .addOnFailureListener {
                    MessageUtils.makeToast(context!!, "Er is een fout opgetreden tijdens het proberen versturen van de e-mail.")
                }
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

    // Function to show the dialog which asks the user how to user wants to edit their profile picture
    private fun showChangeProfilePictureDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Camera") { _, _ -> sendResetPasswordEmail()}
                .setNegativeButton("Gallerij") { dialog, _ -> dialog?.dismiss() }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog?.dismiss() }
                .create()
                .show()
    }

}
