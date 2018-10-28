package com.bramdeconinck.technologysalesmantoolkit.activities

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.fragments.LoginFragment
import com.bramdeconinck.technologysalesmantoolkit.fragments.RegistrationFragment
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceListFragment
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IRegistrationSelected
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), IRegistrationSelected {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val firstFragment: Fragment = if (mAuth.currentUser == null) LoginFragment()
        else ServiceListFragment()

        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, firstFragment)
                .commit()
    }

    override fun onRegistrationLabelSelected() {
        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegistrationFragment())
                .addToBackStack(null)
                .commit()
    }
}