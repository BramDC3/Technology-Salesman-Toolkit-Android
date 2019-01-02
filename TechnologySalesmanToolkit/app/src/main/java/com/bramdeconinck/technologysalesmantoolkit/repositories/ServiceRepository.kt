package com.bramdeconinck.technologysalesmantoolkit.repositories

import com.bramdeconinck.technologysalesmantoolkit.database.daos.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.models.Service

/**
 * The [ServiceRepository] class is used to interact with the [serviceDao].
 */
class ServiceRepository(private val serviceDao: ServiceDao) {
    val services = serviceDao.getAllServices()

    fun insert(services: List<Service>) {
        if (services.isNotEmpty()) deleteAllServices()
        services.forEach { serviceDao.insertService(it) }
    }

    private fun deleteAllServices() { serviceDao.deleteAllServices() }
}
