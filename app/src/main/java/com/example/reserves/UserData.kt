package com.example.reserves

import com.google.gson.annotations.SerializedName

data class UserData (
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("apellido") val apellido: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("documento") val documento: Int?,
    @SerializedName("contraseña") val contraseña: String?
)