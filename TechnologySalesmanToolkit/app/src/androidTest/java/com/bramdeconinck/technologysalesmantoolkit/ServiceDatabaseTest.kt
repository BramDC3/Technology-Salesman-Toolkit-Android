package com.bramdeconinck.technologysalesmantoolkit

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.bramdeconinck.technologysalesmantoolkit.TestUtils.getValue
import com.bramdeconinck.technologysalesmantoolkit.database.daos.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDatabase
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.Timestamp
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ServiceDatabaseTest {

    private lateinit var serviceDatabase: ServiceDatabase
    private lateinit var serviceDao: ServiceDao

    @Before
    fun clearDatabase() {
        serviceDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                ServiceDatabase::class.java).build()

        serviceDao = serviceDatabase.serviceDao()
    }

    @Test
    fun insertServices_allInserted() {
        serviceDao.insertService(Service(id = "UNO", name = "Eerste service", price = 1.00, description = "Beschrijving van eerste service", created = Timestamp.now(), image = "url1", category = Category.Apple))
        serviceDao.insertService(Service(id = "DOS", name = "Tweede service", price = 2.00, description = "Beschrijving van tweede service", created = Timestamp.now(), image = "url2", category = Category.Android))
        serviceDao.insertService(Service(id = "TRES", name = "Derde service", price = 3.00, description = "Beschrijving van derde service", created = Timestamp.now(), image = "url3", category = Category.Windows))

        assertEquals(3, getValue(serviceDao.getAllServices()).size)
    }

    @Test
    fun deleteAllServices_databaseEmpty() {
        serviceDao.insertService(Service(id = "UNO", name = "Eerste service", price = 1.00, description = "Beschrijving van eerste service", created = Timestamp.now(), image = "url1", category = Category.Apple))
        serviceDao.insertService(Service(id = "DOS", name = "Tweede service", price = 2.00, description = "Beschrijving van tweede service", created = Timestamp.now(), image = "url2", category = Category.Android))
        serviceDao.insertService(Service(id = "TRES", name = "Derde service", price = 3.00, description = "Beschrijving van derde service", created = Timestamp.now(), image = "url3", category = Category.Windows))

        serviceDao.deleteAllServices()

        assertEquals(0, getValue(serviceDao.getAllServices()).size)
    }

    @Test
    fun insertInstructions_allInserted() {
        serviceDao.insertInstruction(Instruction(id = "ONE", index = 0, title = "Eerste instructie", serviceId = "UNO", image = "url1", description = "Beschrijving van eerste instructie", content = listOf("step1", "step2", "step3")))
        serviceDao.insertInstruction(Instruction(id = "TWO", index = 0, title = "Tweede instructie", serviceId = "DOS", image = "url2", description = "Beschrijving van tweede instructie", content = listOf("step1", "step2", "step3")))
        serviceDao.insertInstruction(Instruction(id = "THREE", index = 0, title = "Derde instructie", serviceId = "TRES", image = "url3", description = "Beschrijving van derde instructie", content = listOf("step1", "step2", "step3")))

        assertEquals(3, getValue(serviceDao.getAllInstructions()).size)
    }

    @Test
    fun deleteInstructionsByServiceId_InstructionsWithOtherServiceIdsRemain() {
        val serviceId = "UNO"

        serviceDao.insertInstruction(Instruction(id = "ONE", index = 0, title = "Eerste instructie", serviceId = "UNO", image = "url1", description = "Beschrijving van eerste instructie", content = listOf("step1", "step2", "step3")))
        serviceDao.insertInstruction(Instruction(id = "TWO", index = 0, title = "Tweede instructie", serviceId = "DOS", image = "url2", description = "Beschrijving van tweede instructie", content = listOf("step1", "step2", "step3")))
        serviceDao.insertInstruction(Instruction(id = "THREE", index = 0, title = "Derde instructie", serviceId = "TRES", image = "url3", description = "Beschrijving van derde instructie", content = listOf("step1", "step2", "step3")))

        serviceDao.deleteInstructionsByServiceId(serviceId)

        assertEquals(2, getValue(serviceDao.getAllInstructions()).size)

        assertFalse(getValue(serviceDao.getAllInstructions()).any { it.serviceId == serviceId })
    }
}