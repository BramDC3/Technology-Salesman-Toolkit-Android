package com.bramdeconinck.technologysalesmantoolkit.models

import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDao

class ServiceRepository(private val serviceDao: ServiceDao) {
    val services = serviceDao.getAllServices()

    fun insert(services: List<Service>) {
        if (services.isNotEmpty()) clearServices()
        services.forEach { serviceDao.insertService(it) }
    }

    private fun clearServices() {
        serviceDao.deleteAllServices()
    }
}
