package com.bramdeconinck.technologysalesmantoolkit

import android.app.Instrumentation
import android.content.Intent
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils
import com.bramdeconinck.technologysalesmantoolkit.utils.PRIVACY_POLICY
import com.bramdeconinck.technologysalesmantoolkit.utils.SHARED_PREFERENCES_KEY_THEME
import com.bramdeconinck.technologysalesmantoolkit.utils.TEST_WEBSITE
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To run the tests of the [SettingsFragmentTest], an emulator with an active internet connection is required.
 */
@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {

    private lateinit var navController: NavController

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun navigateToSettings() {
        if (FirebaseUtils.firebaseAuth.currentUser == null) {
            FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(existentEmail, existentPassword)
            Thread.sleep(10000)
            FirebaseUtils.firebaseUser = FirebaseUtils.firebaseAuth.currentUser
        }

        navController = mActivityTestRule.activity.findNavController(R.id.main_nav_host_fragment)
        mActivityTestRule.activity.runOnUiThread { navController.navigate(R.id.settingsFragment) }
    }

    @Test
    fun openWebsite() {
        Intents.init()
        val expectedIntent = CoreMatchers.allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW), IntentMatchers.hasData(TEST_WEBSITE))
        Intents.intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))

        onView(ViewMatchers.withId(R.id.btn_settings_website)).perform(ViewActions.click())

        Intents.intended(expectedIntent)
        Intents.release()
    }

    @Test
    fun openPrivacyPolicy() {
        Intents.init()
        val expectedIntent = CoreMatchers.allOf(IntentMatchers.hasAction(Intent.ACTION_VIEW), IntentMatchers.hasData(PRIVACY_POLICY))
        Intents.intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))

        onView(ViewMatchers.withId(R.id.btn_settings_privacypolicy)).perform(ViewActions.click())

        Intents.intended(expectedIntent)
        Intents.release()
    }

    @Test
    fun openSendSuggestionDialog_ClickSendWithoutEnteringText_ShowToast() {
        onView(ViewMatchers.withId(R.id.btn_settings_suggestion)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.title_send_suggestion)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(R.string.dialog_send)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.send_suggestion_empty_error)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun openSendSuggestionDialog_EnterText_Successful() {
        onView(ViewMatchers.withId(R.id.btn_settings_suggestion)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.title_send_suggestion)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withHint(R.string.send_suggestion_hint)).perform(ViewActions.typeText("Leuke app, maar het zou handig zijn om diensten toe te kunnen voegen door documenten in te scannen!"))
        closeSoftKeyboard()
        onView(ViewMatchers.withText(R.string.dialog_send)).perform(ViewActions.click())
        onView(ViewMatchers.withText(R.string.send_suggestion_succes)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun signOut() {
        onView(ViewMatchers.withId(R.id.btn_settings_signout)).perform(ViewActions.scrollTo(), ViewActions.click())
        onView(ViewMatchers.withText(R.string.title_sign_out)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(R.string.dialog_yes)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.btn_login_signIn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun clickDarkModeButton_ChangeTheme() {
        val context = getInstrumentation().targetContext
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val themeBefore = sharedPref.getInt(SHARED_PREFERENCES_KEY_THEME, 0)

        onView(ViewMatchers.withId(R.id.btn_settings_darkmode)).perform(ViewActions.click())
        val themeAfter = sharedPref.getInt(SHARED_PREFERENCES_KEY_THEME, 0)
        assertNotEquals(themeBefore, themeAfter)
        if (themeBefore == 1) assertEquals(2, themeAfter)
        if (themeBefore == 2) assertEquals(1, themeAfter)
    }

    @Test
    fun toggleDarkModeSwitch_ChangeTheme() {
        val context = getInstrumentation().targetContext
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val themeBefore = sharedPref.getInt(SHARED_PREFERENCES_KEY_THEME, 0)

        onView(ViewMatchers.withId(R.id.switch_settings_darkmode)).perform(ViewActions.click())
        val themeAfter = sharedPref.getInt(SHARED_PREFERENCES_KEY_THEME, 0)
        assertNotEquals(themeBefore, themeAfter)
        if (themeBefore == 1) assertEquals(2, themeAfter)
        if (themeBefore == 2) assertEquals(1, themeAfter)
    }
}