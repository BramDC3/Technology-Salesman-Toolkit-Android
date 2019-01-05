package com.bramdeconinck.technologysalesmantoolkit.repositories

import com.bramdeconinck.technologysalesmantoolkit.database.daos.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.models.Service

/**
 * The [ServiceRepository] class is used to interact with the [serviceDao].
 */
class ServiceRepository(private val serviceDao: ServiceDao) {

    /**
     * LiveData with as value a list of all services stored in the Room database.
     */
    val services = serviceDao.getAllServices()

    /**
     * Function to insert services into the Room database and clear it first.
     *
     * @param services: List of [Service] objects that need to be inserted.
     */
    fun insert(services: List<Service>) {
        if (services.isNotEmpty()) deleteAllServices()
        services.forEach { serviceDao.insertService(it) }
    }

    /**
     * Function to delete all services stored in the Room database.
     */
    private fun deleteAllServices() { serviceDao.deleteAllServices() }
}
