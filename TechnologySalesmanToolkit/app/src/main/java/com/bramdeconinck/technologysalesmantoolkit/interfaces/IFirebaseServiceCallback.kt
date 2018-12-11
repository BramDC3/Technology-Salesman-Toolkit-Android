package com.bramdeconinck.technologysalesmantoolkit.interfaces

interface IFirebaseServiceCallback {
    fun showProgress()
    fun hideProgress()
    fun showMessage()
    fun onCallBack(list: List<Any>)
}