package com.bramdeconinck.technologysalesmantoolkit.interfaces

interface FirebaseServiceCallback {
    fun showProgress()
    fun hideProgress()
    fun showServicesMessage()
    fun onServicesCallBack(list: List<Any>)
}