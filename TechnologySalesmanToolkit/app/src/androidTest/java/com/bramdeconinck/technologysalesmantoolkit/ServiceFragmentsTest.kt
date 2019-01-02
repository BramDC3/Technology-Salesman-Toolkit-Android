package com.bramdeconinck.technologysalesmantoolkit

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To run the tests of the [ServiceFragmentsTest], an emulator with an active internet connection is required.
 */
@RunWith(AndroidJUnit4::class)
class ServiceFragmentsTest {

    private lateinit var navController: NavController

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun navigateToServiceList() {
        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInWithEmailAndPassword(existentEmail, existentPassword)
            Thread.sleep(10000)
            firebaseUser = firebaseAuth.currentUser
        }

        navController = mActivityTestRule.activity.findNavController(R.id.main_nav_host_fragment)
        mActivityTestRule.activity.runOnUiThread { navController.navigate(R.id.serviceListFragment) }
    }

    @Test
    fun serviceListLoaded_NotEmpty() {
        onView(ViewMatchers.withText("Corrupte data recupereren")).check(matches(isDisplayed()))
    }

    @Test
    fun serviceListLoaded_GoToServiceDetail() {
        onView(ViewMatchers.withText("Corrupte data recupereren")).perform(ViewActions.click())

        onView(ViewMatchers.withText("Installatie van de benodigde software")).check(matches(isDisplayed()))
    }

}