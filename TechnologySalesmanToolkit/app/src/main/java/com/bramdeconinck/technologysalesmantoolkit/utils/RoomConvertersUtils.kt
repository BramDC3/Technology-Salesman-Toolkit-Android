package com.bramdeconinck.technologysalesmantoolkit.utils

import android.arch.persistence.room.TypeConverter
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformCategoryToInt
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformIntToCategory
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * [RoomConvertersUtils] contains all the utilities used to transform object to be able to be stored in a Room database.
 */
object RoomConvertersUtils {

    /**
     * Converting a [Long] to a [Timestamp].
     */
    @TypeConverter
    @JvmStatic
    fun toTimestamp(value: Long): Timestamp { return Timestamp(Date(value)) }

    /**
     * Converting a [Timestamp] to a [Long].
     */
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(timestamp: Timestamp): Long { return timestamp.toDate().time }

    /**
     * Converting an [Int] to a [Category].
     */
    @TypeConverter
    @JvmStatic
    fun toCategory(value: Int): Category { return transformIntToCategory(value) }

    /**
     * Converting a [Category] to an [Int].
     */
    @TypeConverter
    @JvmStatic
    fun fromCategory(category: Category): Int { return transformCategoryToInt(category) }

    /**
     * Converting a list of [String] to JSON.
     */
    @TypeConverter
    @JvmStatic
    fun fromListOfStringsJson(list: List<String>): String { return Gson().toJson(list) }

    /**
     * Converting JSON to a list of [String].
     */
    @TypeConverter
    @JvmStatic
    fun jsonToListOfStrings(json: String): List<String> { return Gson().fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type) }

}