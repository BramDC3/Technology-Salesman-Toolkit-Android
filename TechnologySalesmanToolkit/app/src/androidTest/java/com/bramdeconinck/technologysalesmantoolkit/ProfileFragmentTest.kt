package com.bramdeconinck.technologysalesmantoolkit

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFamilyname
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFirstname
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    private lateinit var navController: NavController

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun navigateToProfile() {
        if (firebaseAuth.currentUser == null) {
            firebaseAuth.signInWithEmailAndPassword(existentEmail, existentPassword)
            Thread.sleep(10000)
            firebaseUser = firebaseAuth.currentUser
        }

        navController = mActivityTestRule.activity.findNavController(R.id.main_nav_host_fragment)
        mActivityTestRule.activity.runOnUiThread { navController.navigate(R.id.profileFragment) }
    }

    @Test
    fun profileFragmentLoaded_FormIsFilledCorrectly() {
        val firstname = getFirstname(firebaseUser!!.displayName!!)
        val familyname = getFamilyname(firebaseUser!!.displayName!!)
        val email = firebaseUser!!.email!!

        onView(withId(R.id.tv_profile_fullname)).check(matches(withText("$firstname $familyname")))
        onView(withId(R.id.et_profile_firstname)).check(matches(withText(firstname)))
        onView(withId(R.id.et_profile_familyname)).check(matches(withText(familyname)))
        onView(withId(R.id.et_profile_email)).check(matches(withText(email)))
    }

    @Test
    fun profilePictureClicked_OpenChangeProfilePictureDialog() {
        onView(withId(R.id.iv_profile_profile_picture)).perform(click())
        onView(withText(R.string.title_change_profile_picture)).check(matches(isDisplayed()))
    }

    @Test
    fun changePasswordClicked_OpenChangePasswordDialog() {
        onView(withId(R.id.tv_profile_change_password)).perform(ViewActions.scrollTo(), click())
        onView(withText(R.string.title_change_password)).check(matches(isDisplayed()))
    }

    @Test
    fun profileFragmentLoaded_EditButtonIsNotVisible() {
        onView(withId(R.id.btn_profile_edit_profile)).check(matches(not(isDisplayed())))
    }

    @Test
    fun editIconClicked_EditButtonIsVisible() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.btn_profile_edit_profile)).check(matches(isDisplayed()))
    }

    @Test
    fun editButtonClicked_FieldsAreUntouched_ExitEditMode() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.btn_profile_edit_profile)).perform(click())
        onView(withId(R.id.btn_profile_edit_profile)).check(matches(not(isDisplayed())))
    }

    @Test
    fun editProfile_EmptyFields() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.et_profile_firstname)).perform(replaceText(""))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.et_profile_familyname)).perform(replaceText(""))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.et_profile_email)).perform(replaceText(""))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.btn_profile_edit_profile)).perform(click())
        onView(withText(R.string.error_empty_fields)).inRoot(withDecorView(not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(matches(isDisplayed()))
    }

    @Test
    fun editProfile_NoFirstname() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.et_profile_firstname)).perform(replaceText(""))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.btn_profile_edit_profile)).perform(click())
        onView(withText(R.string.error_empty_fields)).inRoot(withDecorView(not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(matches(isDisplayed()))
    }

    @Test
    fun editProfile_NoFamilyname() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.et_profile_familyname)).perform(replaceText(""))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.btn_profile_edit_profile)).perform(click())
        onView(withText(R.string.error_empty_fields)).inRoot(withDecorView(not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(matches(isDisplayed()))
    }

    @Test
    fun editProfile_NoEmail() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.et_profile_email)).perform(replaceText(""))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.btn_profile_edit_profile)).perform(click())
        onView(withText(R.string.error_empty_fields)).inRoot(withDecorView(not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(matches(isDisplayed()))
    }

    @Test
    fun editProfile_InvalidEmail() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.et_profile_email)).perform(replaceText("NotAValidEmailAddress"))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.btn_profile_edit_profile)).perform(click())
        onView(withText(R.string.error_invalid_email)).inRoot(withDecorView(not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(matches(isDisplayed()))
    }

    @Test
    fun editProfile_ShowEditProfileDialog() {
        onView(withId(R.id.action_edit_profile)).perform(click())
        onView(withId(R.id.et_profile_firstname)).perform(replaceText("Andy"))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.et_profile_familyname)).perform(replaceText("Droidon"))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.et_profile_email)).perform(replaceText("andy.droidon@gmail.com"))
        ViewActions.closeSoftKeyboard()
        onView(withId(R.id.btn_profile_edit_profile)).perform(click())
        onView(withText(R.string.title_change_profile)).check(matches(isDisplayed()))
    }

}