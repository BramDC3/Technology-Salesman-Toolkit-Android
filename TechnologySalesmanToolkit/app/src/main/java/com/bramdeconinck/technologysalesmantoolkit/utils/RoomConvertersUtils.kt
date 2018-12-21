package com.bramdeconinck.technologysalesmantoolkit.utils

import android.arch.persistence.room.TypeConverter
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.google.firebase.Timestamp
import java.util.*

object RoomConvertersUtils {

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(Date(it)) }
    }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.toDate()?.time
    }

    @TypeConverter
    @JvmStatic
    fun fromCategory(value: Int?): Category? {
        return when (value) {
            0 -> Category.Windows
            1 -> Category.Android
            2 -> Category.Apple
            else -> Category.Andere
        }
    }

    @TypeConverter
    @JvmStatic
    fun toCategory(category: Category): Int? {
        return when (category) {
            Category.Windows -> 0
            Category.Android -> 1
            Category.Apple -> 2
            Category.Andere -> 3
        }
    }

}