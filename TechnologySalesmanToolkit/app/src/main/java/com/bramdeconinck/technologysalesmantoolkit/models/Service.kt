package com.bramdeconinck.technologysalesmantoolkit.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.firebase.Timestamp

/**
 * The [Entity] annotation is used to indicate that this domain object can be used by Room.
 *
 * @param: tableName signifies the name of the table in which objects of this type will be saved.
 */
@Entity(tableName = "service_table")
data class Service(
        /**
         * Each Entity needs a [PrimaryKey]
         */
        @PrimaryKey val id: String,
        val name: String,
        val description: String,
        val category: Category,
        val created: Timestamp,
        val price: Double,
        val image: String)