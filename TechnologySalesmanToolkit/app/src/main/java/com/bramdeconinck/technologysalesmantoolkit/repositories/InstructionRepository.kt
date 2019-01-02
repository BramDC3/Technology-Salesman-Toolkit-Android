package com.bramdeconinck.technologysalesmantoolkit.repositories

import com.bramdeconinck.technologysalesmantoolkit.database.daos.InstructionDao
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction

/**
 * The [InstructionRepository] class is used to interact with the [instructionDao].
 */
class InstructionRepository(private val instructionDao: InstructionDao) {
    val instructions = instructionDao.getAllInstructions()

    fun insert(instructions: List<Instruction>) { instructions.forEach { instructionDao.insertInstruction(it) } }

    fun deleteInstructionsByServiceId(id: String) { instructionDao.deleteInstructionsByServiceId(id) }
}