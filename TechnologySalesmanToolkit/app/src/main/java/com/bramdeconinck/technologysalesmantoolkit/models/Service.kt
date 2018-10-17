package com.bramdeconinck.technologysalesmantoolkit.models

import com.google.firebase.Timestamp

data class Service(val id: String, val name: String, val description: String, val category: Int, val created: Timestamp, val price: Double, val image: String, val url: String)