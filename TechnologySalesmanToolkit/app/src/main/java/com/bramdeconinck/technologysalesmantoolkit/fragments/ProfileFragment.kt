package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bramdeconinck.technologysalesmantoolkit.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_profile, container, false)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            val firebaseUser: FirebaseUser = mAuth.currentUser!!
            Glide.with(this).load(firebaseUser.photoUrl).into(rootView.img_profile_image)
            rootView.txt_profile_name.setText(firebaseUser.displayName)
            rootView.txt_profile_email.setText(firebaseUser.email)
        }

        return rootView
    }

}
