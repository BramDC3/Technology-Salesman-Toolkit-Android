package com.bramdeconinck.technologysalesmantoolkit.interfaces

import com.bramdeconinck.technologysalesmantoolkit.models.Service

interface IFirebaseCallback {
    fun showProgress()
    fun hideProgress()
    fun showMessage(message: String)
    fun onCallBack(list: MutableList<Service>)
}