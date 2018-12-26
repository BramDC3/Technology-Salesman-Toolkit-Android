package com.bramdeconinck.technologysalesmantoolkit

import android.content.Context
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
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.net.wifi.WifiManager



@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    private lateinit var navController: NavController
    private val validEmail = "bramwarsx@gmail.com"
    private val validPassword = "osocosoc"

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun signOut() {
        firebaseAuth.signOut()
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
    fun signIn_AccountDoesNotExist() {
        onView(ViewMatchers.withId(R.id.et_login_email)).perform(ViewActions.typeText("this.email_address@doesnot.exist"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_login_password)).perform(ViewActions.typeText("MySecurePassword"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.sign_in_error)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun signIn_Success() {
        onView(ViewMatchers.withId(R.id.et_login_email)).perform(ViewActions.typeText(validEmail))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_login_password)).perform(ViewActions.typeText(validPassword))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).perform(ViewActions.click())
        FirebaseAuth.AuthStateListener { auth ->
            assertNotNull(auth.currentUser)
            onView(ViewMatchers.withId(R.id.rv_service_list_services)).check(matches(isDisplayed()))
        }
    }

}