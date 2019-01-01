package com.bramdeconinck.technologysalesmantoolkit.repositories

import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction

class InstructionRepository(private val serviceDao: ServiceDao) {
    val instructions = serviceDao.getAllInstructions()

    fun insert(instructions: List<Instruction>) { instructions.forEach { serviceDao.insertInstruction(it) } }

    fun deleteInstructionsByServiceId(id: String) { serviceDao.deleteInstructionsByServiceId(id) }
}