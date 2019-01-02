package com.bramdeconinck.technologysalesmantoolkit

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * [TestUtils] contains all the utilities needed for testing.
 */
object TestUtils {

    /**
     * Used to check the value inside a (Mutable)LiveData object
     */
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }

}