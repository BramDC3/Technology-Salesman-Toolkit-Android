package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.interfaces.ToolbarTitleChanger
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.SHARED_PREFERENCES_KEY_THEME
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ToolbarTitleChanger {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // Changing the splash theme to the default app theme
        setTheme(R.style.AppTheme)

        // If the user has selected dark mode in the settings
        // The night theme should be enabled by default
        enableDarkMode()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigation with a support action bar and a bottom navigation bar
        // Thankfully, the new Navigation Component helps us with this
        setupNavigation()
    }

    // The behavior for the navigation arrow in the toolbar
    override fun onSupportNavigateUp() = navController.navigateUp()

    // This function is all that is required to set up the navigation of the entire app
    private fun setupNavigation() {

        // The toolbar that is defined in the MainActivity layout is used as support action bar
        setSupportActionBar(main_toolbar)

        // This navigation controller allows us to navigate between fragments
        navController = findNavController(R.id.main_nav_host_fragment)

        // The setup of the action bar and bottom navigation bar with the navigation controller
        setupActionBarWithNavController(navController)
        main_bottom_navigation_view.setupWithNavController(navController)

        // This listener helps us with fragment navigation
        // It prepares them so they're ready to be shown properly
        navController.addOnNavigatedListener { _, destination ->
            when (destination.id) {
                // The home of our nav graph is the LoginFragment, but
                // if users are already logged in, they are supposed to be
                // redirected to the ServiceListFragment
                R.id.serviceListFragment -> {
                    if (firebaseAuth.currentUser == null) {
                        navController.navigate(R.id.notSignedIn)
                        hideToolbarAndBottomNavigation()
                    } else showToolbarAndBottomNavigation()
                }
                R.id.loginFragment -> hideToolbarAndBottomNavigation()
                R.id.registrationFragment -> hideToolbarAndBottomNavigation()
                else -> showToolbarAndBottomNavigation()
            }
        }
    }

    // We don't want to show the toolbar and bottom navigation
    // on the login and registration screen, so we hide them
    private fun hideToolbarAndBottomNavigation() {
        supportActionBar!!.hide()

        with(main_bottom_navigation_view) {
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
        supportActionBar!!.show()

        with(main_bottom_navigation_view) {
            if (visibility == View.GONE && alpha == 0f) {
                animate()
                    .alpha(1f)
                    .withEndAction { visibility = View.VISIBLE }
                    .duration = 0
            }
        }
    }

    // Sets the current theme to the theme chosen by the user
    private fun enableDarkMode() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = sharedPref.getInt(SHARED_PREFERENCES_KEY_THEME, 1)
        if (theme == 2) delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    // Thanks to the navigation component, when you navigate to
    // a fragment, the toolbar title is automatically changed
    // based on the current label. This behaviour is great, but we
    // need something different for the ServiceDetailFragment because
    // the title needs to be the name of the service. This function
    // changes the title of the toolbar.
    override fun updateTitle(title: String?) { supportActionBar!!.title = title }

}