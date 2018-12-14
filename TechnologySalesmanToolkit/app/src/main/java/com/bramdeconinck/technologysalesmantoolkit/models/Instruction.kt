package com.bramdeconinck.technologysalesmantoolkit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Instruction(
        val id: String,
        val title: String,
        val description: String,
        val content: String,
        val image: String,
        val serviceId: String,
        val index: Int)
    : Parcelable