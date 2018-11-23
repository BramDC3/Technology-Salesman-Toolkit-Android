package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.R.id.*
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private  lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private var editable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth.currentUser!!
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_profile, container, false)

            Glide.with(this)
                    .load(firebaseUser.photoUrl ?: R.drawable.default_profile_image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(rootView.img_profile_image)

            rootView.lbl_profile_fullname.text = firebaseUser.displayName
            rootView.txt_profile_firstname.setText(StringUtils.getFirstName(firebaseUser.displayName!!))
            rootView.txt_profile_familyname.setText(StringUtils.getFamilyName(firebaseUser.displayName!!))
            rootView.txt_profile_email.setText(firebaseUser.email)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lbl_profile_change_password.setOnClickListener{
            showChangePasswordDialog(context!!, "Wachtwoord wijzigen", "Bent u zeker dat u uw wachtwoord wilt wijzigen? Als u op 'Ja' drukt, zal er een e-mail verzonden worden waarmee uw wachtwoord opnieuw ingesteld kan worden.")
        }
    }

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

    private fun sendResetPasswordEmail() {
        mAuth.sendPasswordResetEmail(firebaseUser.email!!)
                .addOnSuccessListener {
                    MessageUtils.makeToast(context!!, "Er werd een e-mail naar u verzonden waarmee u uw wachtwoord kunt wijzigen.")
                }
                .addOnFailureListener {
                    MessageUtils.makeToast(context!!, "Er is een fout opgetreden tijdens het proberen versturen van de e-mail.")
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return (when(item?.itemId) {
            R.id.action_edit_profile -> {
                toggleEditMode(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }

    private fun toggleEditMode(item: MenuItem?) {
        editable = !editable
        txt_profile_email.isEnabled = editable
        txt_profile_firstname.isEnabled = editable
        txt_profile_familyname.isEnabled = editable
        if (editable) enterProfileEditMode(item)
        else exitProfileEditMode(item)
    }

    private fun enterProfileEditMode(item: MenuItem?) {
        btn_profile_edit_profile.visibility = View.VISIBLE
        lbl_profile_change_password.visibility = View.GONE
        item?.icon = context?.getDrawable(R.drawable.ic_close_black_24dp)
    }

    private fun exitProfileEditMode(item: MenuItem?) {
        btn_profile_edit_profile.visibility = View.GONE
        lbl_profile_change_password.visibility = View.VISIBLE
        item?.icon = context?.getDrawable(R.drawable.ic_edit_black_24dp)
    }

}
