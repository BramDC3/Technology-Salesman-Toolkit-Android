package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used for handling callbacks when fetching Instruction objects.
 */
interface FirebaseInstructionCallback {

    /**
     * Function to show a message when an error occurred while fetching instructions.
     */
    fun showInstructionsMessage()

    /**
     * Function used after all instructions are retrieved from the Network API.
     *
     * @param [instructions]: List of Instruction objects retrieved from the Network API.
     */
    fun onInstructionsCallBack(instructions: List<Any>)
}