package com.bramdeconinck.technologysalesmantoolkit

import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationFragmentTest {

    private lateinit var navController: NavController

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun signOut() {
        navController = mActivityTestRule.activity.findNavController(R.id.main_nav_host_fragment)
        mActivityTestRule.activity.runOnUiThread { navController.navigate(R.id.registrationFragment) }
    }

    @Test
    fun register_EmptyFields() {
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_NoFirstname() {
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("andy.droidon@gmail.com"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_NoFamilyname() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("andy.droidon@gmail.com"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_NoEmail() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_NoPassword() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("andy.droidon@gmail.com"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_NoRepeatPassword() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("andy.droidon@gmail.com"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_InvalidEmail() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("NotAValidEmailAddress"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_invalid_email)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_PasswordTooShort() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("andy.droidon@gmail.com"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_invalid_password)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_PasswordsDoNotMatch() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("andy.droidon@gmail.com"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("applesucks"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.error_passwords_dont_match)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun register_ValidRegistrationForm() {
        onView(ViewMatchers.withId(R.id.et_registration_firstname)).perform(ViewActions.typeText("Andy"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_familyname)).perform(ViewActions.typeText("Droidon"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_email)).perform(ViewActions.typeText("this.email_address@doesnot.exist"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_password)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.et_registration_repeatPassword)).perform(ViewActions.typeText("iphonessuck"))
        closeSoftKeyboard()
        onView(ViewMatchers.withId(R.id.btn_registration_register)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.title_privacy_policy_dialog)).check(matches(isDisplayed()))
    }

    @Test
    fun goToLogin() {
        pressBack()
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}