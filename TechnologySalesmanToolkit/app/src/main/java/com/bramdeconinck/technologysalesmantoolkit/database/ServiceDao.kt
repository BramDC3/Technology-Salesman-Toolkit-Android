package com.bramdeconinck.technologysalesmantoolkit.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service

@Dao
interface ServiceDao {

    @Insert
    fun insertService(service: Service)

    @Query("SELECT * FROM service_table")
    fun getAllServices(): LiveData<List<Service>>

    @Query("DELETE FROM service_table")
    fun deleteAllServices()

    @Insert
    fun insertInstruction(instruction: Instruction)

    @Query("SELECT * FROM instruction_table")
    fun getAllInstructions(): LiveData<List<Instruction>>

    @Query("DELETE FROM instruction_table WHERE serviceId = :id")
    fun deleteInstructionsByServiceId(id: String)
}