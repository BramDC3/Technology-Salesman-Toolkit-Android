package com.bramdeconinck.technologysalesmantoolkit.database.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction

/**
 * The [InstructionDao] is a Data Access Object where the database interactions are defined.
 * They can include a variety of query methods for [Instruction] objects.
 */
@Dao
interface InstructionDao {

    @Insert
    fun insertInstruction(instruction: Instruction)

    @Query("SELECT * FROM instruction_table")
    fun getAllInstructions(): LiveData<List<Instruction>>

    @Query("DELETE FROM instruction_table WHERE serviceId = :id")
    fun deleteInstructionsByServiceId(id: String)

}