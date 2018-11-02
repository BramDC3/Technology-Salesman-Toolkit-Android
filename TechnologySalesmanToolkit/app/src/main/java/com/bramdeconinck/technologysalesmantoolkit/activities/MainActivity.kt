package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.R.id.nav_host_fragment
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // The Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance()


        // The home of our nav graph is the ServiceListFragment, but
        // Users need to be logged in in order to do so
        // This method redirects users to the LoginFragment if they aren't logged in
        findNavController(nav_host_fragment).addOnNavigatedListener { _, destination ->
            when (destination.id) {
                R.id.serviceListFragment -> {
                    if (mAuth.currentUser == null) {
                        findNavController(nav_host_fragment).popBackStack(R.id.serviceListFragment, true)
                        findNavController(nav_host_fragment).navigate(R.id.loginFragment)
                    }
                }
            }
        }
    }

}