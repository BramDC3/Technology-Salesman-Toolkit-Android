package com.bramdeconinck.technologysalesmantoolkit.interfaces

interface IFirebaseServiceCallback {
    fun showProgress()
    fun hideProgress()
    fun showServicesMessage()
    fun onServicesCallBack(list: List<Any>)
}