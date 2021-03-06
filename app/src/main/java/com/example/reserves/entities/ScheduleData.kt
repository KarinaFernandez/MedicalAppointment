package com.example.reserves.entities

import com.google.gson.annotations.SerializedName

data class ScheduleData (
    @SerializedName("_id") val _id: String?,
    @SerializedName("medico") val medico: String?,
    @SerializedName("usuario") val usuario: String?,
    @SerializedName("fecha") val fecha: String?,
    @SerializedName("latitud") val latitud: Double?,
    @SerializedName("longitud") val longitud: Double?
)