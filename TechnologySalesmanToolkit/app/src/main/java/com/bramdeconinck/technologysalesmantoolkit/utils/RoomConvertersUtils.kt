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
     *
     * @param value: [Long] that needs to be converted to a [Timestamp].
     * @return: [Timestamp] with the value of [value].
     */
    @TypeConverter
    @JvmStatic
    fun toTimestamp(value: Long): Timestamp { return Timestamp(Date(value)) }

    /**
     * Converting a [Timestamp] to a [Long].
     *
     * @param timestamp: [Timestamp] that needs to be converted to a [Long].
     * @return: [Long] with the value of [timestamp].
     */
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(timestamp: Timestamp): Long { return timestamp.toDate().time }

    /**
     * Converting an [Int] to a [Category].
     *
     * @param value: [Int] that needs to be converted to a [Category].
     * @return: [Category] with the value of [value].
     */
    @TypeConverter
    @JvmStatic
    fun toCategory(value: Int): Category { return transformIntToCategory(value) }

    /**
     * Converting a [Category] to an [Int].
     *
     * @param category: [Category] that needs to be converted to an Integer.
     * @return: Integer with the value of [category].
     */
    @TypeConverter
    @JvmStatic
    fun fromCategory(category: Category): Int { return transformCategoryToInt(category) }

    /**
     * Converting a list of [String] to JSON.
     *
     * @param list: List of [String] that needs to be converted to json.
     * @return: [list] as json.
     */
    @TypeConverter
    @JvmStatic
    fun fromListOfStringsJson(list: List<String>): String { return Gson().toJson(list) }

    /**
     * Converting JSON to a list of [String].
     *
     * @param json: Json that needs to be converted to a list of [String].
     * @return: List of [String] from the json.
     */
    @TypeConverter
    @JvmStatic
    fun jsonToListOfStrings(json: String): List<String> { return Gson().fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type) }

}