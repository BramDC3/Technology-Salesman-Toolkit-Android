package com.bramdeconinck.technologysalesmantoolkit.database.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction

/**
 * The [InstructionDao] is a Data Access Object where database instructions are defined.
 * They can include a variety of query methods for [Instruction] objects.
 */
@Dao
interface InstructionDao {

    /**
     * Function to insert a [Instruction] into the Room database.
     */
    @Insert
    fun insertInstruction(instruction: Instruction)

    /**
     * Function to fetch all [Instruction] objects stored in the Room database.
     */
    @Query("SELECT * FROM instruction_table")
    fun getAllInstructions(): LiveData<List<Instruction>>

    /**
     * Function to delete all [Instruction] objects with a given serviceId stored in the Room database.
     */
    @Query("DELETE FROM instruction_table WHERE serviceId = :id")
    fun deleteInstructionsByServiceId(id: String)

}