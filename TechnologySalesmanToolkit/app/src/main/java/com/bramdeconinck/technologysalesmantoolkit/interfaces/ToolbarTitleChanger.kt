package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used to change the title of the action bar to the name of the Service object that's currently displayed.
 */
interface ToolbarTitleChanger {

    /**
     * Function to change the title of the action bar.
     *
     * @param [title]: Title for the action bar.
     */
    fun updateTitle(title: String?)

}