package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.fragments.LoginFragment
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceListFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}