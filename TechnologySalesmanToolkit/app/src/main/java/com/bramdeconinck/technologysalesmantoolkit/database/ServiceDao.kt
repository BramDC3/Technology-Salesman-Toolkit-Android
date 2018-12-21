package com.bramdeconinck.technologysalesmantoolkit.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bramdeconinck.technologysalesmantoolkit.models.Service

@Dao
interface ServiceDao {

    @Insert
    fun insert(service: Service)

    @Query("SELECT * FROM service_table")
    fun getAllServices(): LiveData<List<Service>>

    @Query("DELETE FROM service_table")
    fun deleteAllServices()
}