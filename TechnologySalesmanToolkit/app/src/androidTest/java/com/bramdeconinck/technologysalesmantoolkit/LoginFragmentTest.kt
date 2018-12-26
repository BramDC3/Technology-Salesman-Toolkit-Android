package com.bramdeconinck.technologysalesmantoolkit

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun signIn_NoEmail() {
        Espresso.onView(ViewMatchers.withId(R.id.et_login_password)).perform(ViewActions.typeText("osocosoc"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.btn_login_signIn)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.error_empty_fields)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView)))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}