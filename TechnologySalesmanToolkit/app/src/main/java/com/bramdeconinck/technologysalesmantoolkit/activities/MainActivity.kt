package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToolbarTitleListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IToolbarTitleListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This object is used for Firebase authentication
        // It is used to see if the user is logged in and to get their information
        mAuth = FirebaseAuth.getInstance()

        // Navigation with a support action bar and a bottom navigation bar
        // Thankfully, the new Navigation Component helps us with this
        setupNavigation()
    }

    // This function is all that is required to set up the navigation of the entire app
    private fun setupNavigation() {

        // The toolbar that is defined in the MainActivity layout is used as support action bar
        setSupportActionBar(custom_toolbar)

        // This navigation controller allows us to navigate between fragments
        navController = findNavController(R.id.nav_host_fragment)

        // The setup of the action bar and bottom navigation bar with the navigation controller
        setupActionBarWithNavController(navController)
        bottom_navigation_view.setupWithNavController(navController)

        // This listener helps us with fragment navigation
        // It prepares them so they're ready to be shown properly
        navController.addOnNavigatedListener { _, destination ->
            when (destination.id) {
                // The home of our nav graph is the LoginFragment, but
                // if users are already logged in, they are supposed to be
                // redirected to the ServiceListFragment
                R.id.serviceListFragment -> {
                    if (mAuth.currentUser == null) {
                        navController.navigate(R.id.notSignedIn)
                        hideToolbarAndBottomNavigation()
                    } else {
                        showToolbarAndBottomNavigation()
                    }
                }
                R.id.loginFragment -> hideToolbarAndBottomNavigation()
            }
        }
    }

    // The behavior for the navigation arrow in the toolbar
    override fun onSupportNavigateUp() = navController.navigateUp()

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

    // Thanks to the navigation component, when you navigate to
    // a fragment, the toolbar title is automatically changed
    // based on the current label. This behaviour is great, but we
    // need something different for the ServiceDetailFragment because
    // the title needs to be the name of the service. This function
    // changes the title of the toolbar.
    override fun updateTitle(title: String?) {
        supportActionBar?.title = title
    }

}