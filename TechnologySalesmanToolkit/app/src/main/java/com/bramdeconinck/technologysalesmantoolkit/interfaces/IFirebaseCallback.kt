package com.bramdeconinck.technologysalesmantoolkit.interfaces

import com.bramdeconinck.technologysalesmantoolkit.models.Service

interface IFirebaseCallback {
    fun onCallBack(list: MutableList<Service>)
}