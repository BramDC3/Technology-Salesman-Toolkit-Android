package com.bramdeconinck.technologysalesmantoolkit.interfaces

import com.bramdeconinck.technologysalesmantoolkit.models.Service

interface IFirebaseCallback {
    fun showProgress()
    fun hideProgress()
    fun showMessage()
    fun onCallBack(list: MutableList<Service>)
}