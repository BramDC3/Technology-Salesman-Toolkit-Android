package com.bramdeconinck.technologysalesmantoolkit.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service(
        val id: String,
        val name: String,
        val description: String,
        val category: Category,
        val created: Timestamp,
        val price: Double,
        val image: String,
        val url: String)
    : Parcelable