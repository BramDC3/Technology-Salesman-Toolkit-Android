package com.bramdeconinck.technologysalesmantoolkit.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.utils.RoomConvertersUtils

@Database(entities = [Service::class, Instruction::class], version = 1)
@TypeConverters(RoomConvertersUtils::class)
abstract class ServiceDatabase : RoomDatabase() {

    abstract fun serviceDao() : ServiceDao

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