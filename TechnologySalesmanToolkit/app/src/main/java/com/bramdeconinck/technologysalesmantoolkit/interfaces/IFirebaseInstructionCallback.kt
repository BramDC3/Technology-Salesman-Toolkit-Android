package com.bramdeconinck.technologysalesmantoolkit.interfaces

interface IFirebaseInstructionCallback {
    fun showInstructionsMessage()
    fun onInstructionsCallBack(list: List<Any>)
}