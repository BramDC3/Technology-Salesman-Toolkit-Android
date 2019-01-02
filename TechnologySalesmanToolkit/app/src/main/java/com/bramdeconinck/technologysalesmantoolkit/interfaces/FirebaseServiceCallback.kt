package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used for handling callbacks when fetching Service objects.
 */
interface FirebaseServiceCallback {
    fun showProgress()
    fun hideProgress()
    fun showServicesMessage()
    fun onServicesCallBack(list: List<Any>)
}