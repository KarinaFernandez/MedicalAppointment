package com.example.reserves

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EndpointsInterface {

   // @Headers("Content-Type: application/json")
    @POST("usuarios")
    fun addUser(@Body userData: UserData): Call<UserData>

}