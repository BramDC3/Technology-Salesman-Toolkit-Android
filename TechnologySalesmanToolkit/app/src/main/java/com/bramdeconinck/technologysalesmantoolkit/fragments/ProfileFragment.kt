package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
            Glide.with(this).load(firebaseUser.photoUrl ?: DEFAULT_PROFILE_IMAGE).apply(RequestOptions.circleCropTransform()).into(rootView.img_profile_image)
            rootView.txt_profile_name.setText(firebaseUser.displayName)
            rootView.txt_profile_email.setText(firebaseUser.email)
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).supportActionBar?.title = getString(R.string.title_profile_fragment)
    }

    companion object {
        const val DEFAULT_PROFILE_IMAGE: String = "https://firebasestorage.googleapis.com/v0/b/technology-salesman-toolkit.appspot.com/o/default_profile_image.jpg?alt=media&token=24bc7608-c16d-4c6d-8c7c-dcb10bd4d3d5"
    }

}
