package com.bramdeconinck.technologysalesmantoolkit.models

import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDao

class ServiceRepository(private val serviceDao: ServiceDao) {
    val services = serviceDao.getAllServices()

    fun insert(services: List<Service>) {
        if (services.isNotEmpty()) clearDatabase()
        services.forEach { serviceDao.insert(it) }
    }

    private fun clearDatabase() {
        serviceDao.deleteAllServices()
    }
}
