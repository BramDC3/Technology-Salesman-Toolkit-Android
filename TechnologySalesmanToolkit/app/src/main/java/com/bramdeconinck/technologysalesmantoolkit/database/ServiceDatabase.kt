package com.bramdeconinck.technologysalesmantoolkit.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.bramdeconinck.technologysalesmantoolkit.database.daos.InstructionDao
import com.bramdeconinck.technologysalesmantoolkit.database.daos.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.utils.RoomConvertersUtils

/**
 * The [ServiceDatabase] used to cache [Service] and [Instruction] objects.
 */
@Database(entities = [Service::class, Instruction::class], version = 1, exportSchema = false)
@TypeConverters(RoomConvertersUtils::class)
abstract class ServiceDatabase : RoomDatabase() {

    /**
     * The [ServiceDao] used to interact with the [Service] objects of the [RoomDatabase].
     */
    abstract fun serviceDao() : ServiceDao

    /**
     * The [InstructionDao] used to interact with the [Instruction] objects of the [RoomDatabase].
     */
    abstract fun instructionDao() : InstructionDao

    /**
     * The instance of the [ServiceDatabase].
     * If doesn't exist yet, it is initialized before it is returned.
     */
    companion object {
        private var instance : ServiceDatabase? = null

        fun getInstance(context: Context) : ServiceDatabase {
            if (instance != null) return instance!!
            val newInstance = Room.databaseBuilder(
                    context,
                    ServiceDatabase::class.java,
                    "service_database"
            ).build()
            instance = newInstance
            return newInstance
        }
    }
}