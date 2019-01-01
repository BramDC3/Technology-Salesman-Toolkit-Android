package com.bramdeconinck.technologysalesmantoolkit.repositories

import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.models.Service

class ServiceRepository(private val serviceDao: ServiceDao) {
    val services = serviceDao.getAllServices()

    fun insert(services: List<Service>) {
        if (services.isNotEmpty()) deleteAllServices()
        services.forEach { serviceDao.insertService(it) }
    }

    private fun deleteAllServices() { serviceDao.deleteAllServices() }
}
