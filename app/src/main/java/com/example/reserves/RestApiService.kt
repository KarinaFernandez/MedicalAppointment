package com.example.reserves

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RestApiService {
    fun createUser(userData: UserData, onResult: (UserData?) -> Unit){
        val retrofit = ServiceBuilder.buildService(EndpointsInterface::class.java)
        retrofit.addUser(userData).enqueue(
            object : Callback<UserData> {
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<UserData>, response: Response<UserData>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }
}