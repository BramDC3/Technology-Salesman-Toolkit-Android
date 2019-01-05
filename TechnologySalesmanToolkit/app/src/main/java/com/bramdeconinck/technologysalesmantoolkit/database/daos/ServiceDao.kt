package com.bramdeconinck.technologysalesmantoolkit.database.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service

/**
 * The [ServiceDao] is a Data Access Object where database interactions are defined.
 * They can include a variety of query methods for [Service] objects.
 */
@Dao
interface ServiceDao {

    /**
     * Function to insert a [Service] into the Room database.
     */
    @Insert
    fun insertService(service: Service)

    /**
     * Function to fetch all [Service] objects stored in the Room database.
     */
    @Query("SELECT * FROM service_table")
    fun getAllServices(): LiveData<List<Service>>

    /**
     * Function to delete all [Service] objects stored in the Room database.
     */
    @Query("DELETE FROM service_table")
    fun deleteAllServices()

}