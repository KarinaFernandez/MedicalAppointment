package com.example.reserves.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.reserves.converters.ListConverter
import com.google.gson.annotations.SerializedName

@Entity
data class DoctorData (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @SerializedName("_id") val _id: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("apellido") val apellido: String?,
    @SerializedName("titulo") val titulo: String?,

    @TypeConverters(ListConverter::class)
    //  @SerializedName("especialidades") var especialidades: List<SpecialtyData>?,

    @SerializedName("puntuacion") val puntuacion: Int?,

    @TypeConverters(ListConverter::class)
    //@SerializedName("comentarios") val comentarios: List<CommentData>?,

    @SerializedName("centroMedico") val centroMedico: String?
)