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

/**
 * The [MainActivity] is an entry point to the application.
 * It's the only activity of this project.
 */
class MainActivity : AppCompatActivity(), ToolbarTitleChanger {

    /**
     * The [navController] is a part of the Navigation Component.
     * It is used to navigate from and to destinations of the navigation graph.
     */
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Changing the splash theme of the app to the default app theme.
         */
        setTheme(R.style.AppTheme)

        setDayNightMode()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
    }

    /**
     * Setup for the behavior of the navigation arrow in the action bar.
     */
    override fun onSupportNavigateUp() = navController.navigateUp()

    /**
     * Setup for the navigation of the entire application.
     * The Navigation Component simplifies navigation with the [navController].
     * It also does the setup of the action bar and bottom navigation view in an easy manner.
     */
    private fun setupNavigation() {

        navController = findNavController(R.id.main_nav_host_fragment)

        setSupportActionBar(main_toolbar)
        setupActionBarWithNavController(navController)
        main_bottom_navigation_view.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                /**
                 * Conditional navigation: if the user is logged in, he/she is allowed to navigate to the ServiceListFragment.
                 * If the user isn't logged in, he/she gets redirected to the LoginFragment.
                 */
                R.id.serviceListFragment -> {
                    if (firebaseAuth.currentUser == null) {
                        navController.navigate(R.id.notSignedIn)
                        hideActionBarAndBottomNavigationView()
                    } else showActionBarAndBottomNavigationView()
                }

                /**
                 * When the user navigates to the LoginFragment or the RegistrationFragment, the action bar and bottom navigation view need to be hidden.
                 * If the user navigates to any other fragment, the action bar and bottom navigation view need to be shown.
                 */
                R.id.loginFragment -> hideActionBarAndBottomNavigationView()
                R.id.registrationFragment -> hideActionBarAndBottomNavigationView()
                else -> showActionBarAndBottomNavigationView()
            }
        }
    }

    /**
     * Function to hide the action bar and bottom navigation view.
     */
    private fun hideActionBarAndBottomNavigationView() {
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

    /**
     * Function to show the action bar and bottom navigation view.
     */
    private fun showActionBarAndBottomNavigationView() {
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

    /**
     * Changing the theme to day or night mode based on the preference of the user.
     * The default theme is day mode.
     */
    private fun setDayNightMode() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = sharedPref.getInt(SHARED_PREFERENCES_KEY_THEME, 1)
        if (theme == 2) delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    /**
     * The Navigation Component automatically changes the title of the action bar to the label of the current destination.
     * When the current destination is the ServiceDetailFragment,
     * this function changes the title to the name of the Service instead of the label of the destination.
     *
     * @param title: String used as title of the action bar.
     */
    override fun updateTitle(title: String?) { supportActionBar!!.title = title }

}