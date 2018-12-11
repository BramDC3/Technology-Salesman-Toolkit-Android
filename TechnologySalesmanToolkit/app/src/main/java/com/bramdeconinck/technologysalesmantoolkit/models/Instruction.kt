package com.bramdeconinck.technologysalesmantoolkit.models

data class Instruction(
        val id: String,
        val title: String,
        val description: String,
        val content: String,
        val image: String,
        val serviceId: String,
        val index: Int)