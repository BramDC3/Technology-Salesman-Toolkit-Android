package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used to create and display toasts without always having to give context with them.
 */
interface ToastMaker {

    /**
     * Function for showing a toast message.
     *
     * @param [message]: String resource Id.
     */
    fun showToast(message: Int)
}