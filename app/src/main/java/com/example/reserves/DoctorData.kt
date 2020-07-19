package com.example.reserves

import com.google.gson.annotations.SerializedName

data class DoctorsData (
    var doctors: List<DoctorData>?
)

data class DoctorData (
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("apellido") val apellido: String?,
    @SerializedName("centroMedico") val centroMedico: String?,
    @SerializedName("especialidades") var especialidades: List<SpecialtyData>?
)

data class SpecialtyData (
    @SerializedName("nombre") val nombre: String?
)