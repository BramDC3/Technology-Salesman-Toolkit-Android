package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used for handling callbacks when fetching Instruction objects.
 */
interface FirebaseInstructionCallback {
    fun showInstructionsMessage()
    fun onInstructionsCallBack(list: List<Any>)
}