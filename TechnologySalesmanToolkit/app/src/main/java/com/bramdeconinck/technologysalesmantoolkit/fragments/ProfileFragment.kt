package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_profile, container, false)

        if (mAuth.currentUser != null) {
            val firebaseUser: FirebaseUser = mAuth.currentUser!!

            Glide.with(this)
                    .load(firebaseUser.photoUrl ?: R.drawable.default_profile_image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(rootView.img_profile_image)

            rootView.lbl_profile_fullname.text = firebaseUser.displayName
            rootView.txt_profile_firstname.setText(StringUtils.getFirstName(firebaseUser.displayName!!))
            rootView.txt_profile_familyname.setText(StringUtils.getFamilyName(firebaseUser.displayName!!))
            rootView.txt_profile_email.setText(firebaseUser.email)
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
