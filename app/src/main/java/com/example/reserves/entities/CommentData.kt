package com.example.reserves.entities

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class CommentData (
    @SerializedName("_id") val _id: String?,
    @SerializedName("comentario") val comentario: String?,
    @SerializedName("fecha") val fecha: Date?,
    @SerializedName("email") val email: String?,
    @SerializedName("medico") val medico: DoctorData?
)