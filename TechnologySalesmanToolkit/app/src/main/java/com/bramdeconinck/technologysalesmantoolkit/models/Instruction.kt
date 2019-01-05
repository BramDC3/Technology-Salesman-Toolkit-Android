package com.bramdeconinck.technologysalesmantoolkit.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * The [Entity] annotation is used to indicate that this domain object can be used by Room.
 *
 * @param: tableName signifies the name of the table in which objects of this type will be saved.
 */
@Entity(tableName = "instruction_table")
data class Instruction(
        /**
         * Each Entity needs a [PrimaryKey]
         */
        @PrimaryKey val id: String,
        val title: String,
        val description: String,
        val content: List<String>,
        val image: String,
        val serviceId: String,
        val index: Int)