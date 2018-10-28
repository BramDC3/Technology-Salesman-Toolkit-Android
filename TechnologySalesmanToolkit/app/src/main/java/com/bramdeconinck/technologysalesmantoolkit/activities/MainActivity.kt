package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.fragments.LoginFragment
import com.bramdeconinck.technologysalesmantoolkit.fragments.RegistrationFragment
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IRegistrationSelected

class MainActivity : AppCompatActivity(), IRegistrationSelected {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginFragment = LoginFragment()

        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, loginFragment)
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