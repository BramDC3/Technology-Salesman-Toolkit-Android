package com.bramdeconinck.technologysalesmantoolkit.repositories

import com.bramdeconinck.technologysalesmantoolkit.database.daos.InstructionDao
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction

/**
 * The [InstructionRepository] class is used to interact with the [instructionDao].
 */
class InstructionRepository(private val instructionDao: InstructionDao) {

    /**
     * LiveData with as value a list of all instructions stored in the Room database.
     */
    val instructions = instructionDao.getAllInstructions()

    /**
     * Function to insert instructions into the Room database.
     *
     * @param instructions: List of [Instruction] objects that need to be inserted.
     */
    fun insert(instructions: List<Instruction>) { instructions.forEach { instructionDao.insertInstruction(it) } }

    /**
     * Function to delete all instructions of a given Service stored in the Room database.
     *
     * @param id: Id of the service who's instructions need to be deleted.
     */
    fun deleteInstructionsByServiceId(id: String) { instructionDao.deleteInstructionsByServiceId(id) }
}