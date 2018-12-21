package com.bramdeconinck.technologysalesmantoolkit.utils

import android.arch.persistence.room.TypeConverter
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

}