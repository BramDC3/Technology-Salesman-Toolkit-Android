package com.bramdeconinck.technologysalesmantoolkit.models

import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDao

class ServiceRepository(private val serviceDao: ServiceDao) {
    val services = serviceDao.getAllServices()

    fun insert(services: List<Service>) {
        if (services.isNotEmpty()) serviceDao.deleteAllServices()
        services.forEach { serviceDao.insert(it) }
    }

    fun clearDatabase() {
        serviceDao.deleteAllServices()
    }
}
