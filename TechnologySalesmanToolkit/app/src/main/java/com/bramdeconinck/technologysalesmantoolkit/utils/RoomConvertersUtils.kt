package com.bramdeconinck.technologysalesmantoolkit.utils

import android.arch.persistence.room.TypeConverter
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformCategoryToInt
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformIntToCategory
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object RoomConvertersUtils {

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long): Timestamp { return Timestamp(Date(value))
    }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(timestamp: Timestamp): Long { return timestamp.toDate().time }

    @TypeConverter
    @JvmStatic
    fun fromCategory(value: Int): Category { return transformIntToCategory(value) }

    @TypeConverter
    @JvmStatic
    fun toCategory(category: Category): Int { return transformCategoryToInt(category) }

    @TypeConverter
    @JvmStatic
    fun fromListOfStringsJson(list: List<String>): String { return Gson().toJson(list) }

    @TypeConverter
    @JvmStatic
    fun jsonToListOfStrings(json: String): List<String> { return Gson().fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type) }

}