package com.example.reserves

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EndpointsInterface {

    @POST("login")
    fun loginUser(@Body userData: UserData): Call<UserData>

    @POST("usuarios")
    fun createUser(@Body userData: UserData): Call<UserData>

    @GET("medicos")
    fun getDoctors(): Call<List<DoctorData>>
}