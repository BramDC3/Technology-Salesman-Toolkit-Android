package com.bramdeconinck.technologysalesmantoolkit.interfaces

interface FirebaseInstructionCallback {
    fun showInstructionsMessage()
    fun onInstructionsCallBack(list: List<Any>)
}