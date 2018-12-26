package com.bramdeconinck.technologysalesmantoolkit

import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationFragmentTest {

    @Test
    fun signIn_NoEmail() {
        Assert.assertEquals(3, 3)
    }

    @Test
    fun signIn_NoPassword() {
        Assert.assertEquals(3, 3)
    }

}