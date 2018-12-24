package com.bramdeconinck.technologysalesmantoolkit.models

import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDao

class InstructionRepository(private val serviceDao: ServiceDao) {
    val instructions = serviceDao.getAllInstructions()

    fun insert(instructions: List<Instruction>) { instructions.forEach { serviceDao.insertInstruction(it) } }

    fun deleteInstructionsByServiceId(id: String) { serviceDao.deleteInstructionsByServiceId(id) }
}