package com.bramdeconinck.technologysalesmantoolkit.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

/**
 * The @Entity annotation is used to indicate that this domain object can be used by Room.
 * The tableName parameter signifies the name of the table in which objects of this type will be saved.
 */
@Parcelize
@Entity(tableName = "service_table")
data class Service(
        //Each Entity requires a primary key
        @PrimaryKey val id: String,
        val name: String,
        val description: String,
        val category: Category,
        val created: Timestamp,
        val price: Double,
        val image: String,
        val url: String)
    : Parcelable