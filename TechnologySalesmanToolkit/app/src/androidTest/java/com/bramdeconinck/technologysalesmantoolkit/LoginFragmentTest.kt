package com.bramdeconinck.technologysalesmantoolkit

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    private lateinit var navController: NavController

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun signOut() {
        navController = mActivityTestRule.activity.findNavController(R.id.main_nav_host_fragment)
        mActivityTestRule.activity.runOnUiThread { navController.navigate(R.id.loginFragment) }
    }

    @Test
    fun signIn_EmptyFields() {
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun signIn_NoPassword() {
        onView(ViewMatchers.withId(R.id.et_login_email)).perform(ViewActions.typeText("bram@bramdeconinck.com"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun signIn_NoEmail() {
        onView(ViewMatchers.withId(R.id.et_login_password)).perform(ViewActions.typeText("MySecurePassword"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun signIn_InvalidEmail() {
        onView(ViewMatchers.withId(R.id.et_login_email)).perform(ViewActions.typeText("NotAValidEmailAddress"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_login_password)).perform(ViewActions.typeText("MySecurePassword"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_invalid_email)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun goToRegistration() {
        onView(ViewMatchers.withId(R.id.tv_login_goToRegistration)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_registration_register)).check(matches(isDisplayed()))
    }

}