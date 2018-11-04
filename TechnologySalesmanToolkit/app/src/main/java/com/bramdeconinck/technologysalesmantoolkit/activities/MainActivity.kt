package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // The Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance()

        // Using the custom toolbar as support action bar
        setSupportActionBar(custom_toolbar)

        // The home of our nav graph is the ServiceListFragment, but
        // Users need to be logged in in order to do so
        // This method redirects users to the LoginFragment if they aren't logged in
        findNavController(R.id.nav_host_fragment).addOnNavigatedListener { _, destination ->
            when (destination.id) {
                R.id.loginFragment -> {
                    if (mAuth.currentUser != null) {
                        findNavController(R.id.nav_host_fragment).popBackStack(R.id.loginFragment, true)
                        findNavController(R.id.nav_host_fragment).navigate(R.id.serviceListFragment)
                    } else {
                        hideToolbarAndBottomNavigation()
                    }
                }
                R.id.registrationFragment -> hideToolbarAndBottomNavigation()
                else -> showToolbarAndBottomNavigation()
            }
        }
    }

    // We don't want to show the toolbar and bottom navigation
    // On the login and registration screen, so we hide them
    private fun hideToolbarAndBottomNavigation() {
        supportActionBar?.hide()
        with(bottom_navigation_view) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                        .alpha(0f)
                        .withEndAction { visibility = View.GONE }
                        .duration = 0
            }
        }
    }

    // We do want to show the toolbar and bottom navigation
    // On the other screens, so we make them visible again
    private fun showToolbarAndBottomNavigation() {
        supportActionBar?.show()
        with(bottom_navigation_view) {
            visibility = View.VISIBLE
            animate()
                    .alpha(1f)
                    .duration = 0
        }
    }

}