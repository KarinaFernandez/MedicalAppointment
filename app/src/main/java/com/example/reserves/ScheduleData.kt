package com.example.reserves

import com.google.gson.annotations.SerializedName

data class ScheduleData (
    @SerializedName("_id") val _id: String?,
    @SerializedName("medico") val medico: String?,
    @SerializedName("usuario") val usuario: String?,
    @SerializedName("fecha") val fecha: String?
)