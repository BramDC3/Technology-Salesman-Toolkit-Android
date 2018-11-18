package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance()

        // Using a custom toolbar as support action bar
        setSupportActionBar(custom_toolbar)

        // This function helps us with fragment navigation,
        // it prepares them so they're ready to be shown properly
        findNavController(R.id.nav_host_fragment).addOnNavigatedListener { _, destination ->
            when (destination.id) {
                // The home of our nav graph is the LoginFragment, but
                // if users are already logged in, they are supposed to be
                // redirected to the ServiceListFragment
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

        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation_view.setupWithNavController(navController)
    }

    // We don't want to show the toolbar and bottom navigation
    // on the login and registration screen, so we hide them
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
    // on the other screens, so we make them visible again
    private fun showToolbarAndBottomNavigation() {
        supportActionBar?.show()
        with(bottom_navigation_view) {
            visibility = View.VISIBLE
            animate()
                    .alpha(1f)
                    .duration = 0
        }
    }

    // The behavior for the navigation arrow in the toolbar
    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

}