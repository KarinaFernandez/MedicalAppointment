package com.example.reserves.network

import com.example.reserves.entities.DoctorData
import com.example.reserves.entities.ScheduleData
import com.example.reserves.entities.UserData
import retrofit2.Call
import retrofit2.http.*

interface EndpointsInterface {

    @POST("login")
    fun loginUser(@Body userData: UserData): Call<UserData>

    @POST("usuarios")
    fun createUser(@Body userData: UserData): Call<UserData>

    @GET("medicos")
    fun getDoctors(): Call<ArrayList<DoctorData>>

    @PUT("medicos/{id}")
    fun updateDoctor(@Path("id") id: String?, @Body doctorData: DoctorData): Call<DoctorData>

    @POST("reservas")
    fun createSchedule(@Body scheduleData: ScheduleData): Call<ScheduleData>
}