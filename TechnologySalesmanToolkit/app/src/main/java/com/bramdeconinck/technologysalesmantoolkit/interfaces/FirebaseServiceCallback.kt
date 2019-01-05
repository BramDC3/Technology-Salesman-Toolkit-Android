package com.bramdeconinck.technologysalesmantoolkit.interfaces

/**
 * Interface used for handling callbacks when fetching Service objects.
 */
interface FirebaseServiceCallback {

    /**
     * Function that indicates the fetching of services has begun.
     */
    fun showProgress()

    /**
     * Function that indicates the fetching of services has stopped.
     * This can be either on success or on failure.
     */
    fun hideProgress()

    /**
     * Function to show a message when an error occurred while fetching instructions.
     */
    fun showServicesMessage()

    /**
     * Function used after all services are retrieved from the Network API.
     *
     * @param [services]: List of Service objects retrieved from the Network API.
     */
    fun onServicesCallBack(services: List<Any>)
}