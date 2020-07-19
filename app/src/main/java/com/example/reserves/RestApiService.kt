package com.example.reserves

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RestApiService {
    fun loginUser(userData: UserData, onResult: (UserData?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(EndpointsInterface::class.java)

        retrofit.loginUser(userData).enqueue(
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

    fun createUser(userData: UserData, onResult: (UserData?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(EndpointsInterface::class.java)

        retrofit.createUser(userData).enqueue(
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

    fun getDoctors(onResult: (List<DoctorData>?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(EndpointsInterface::class.java)


        retrofit.getDoctors().enqueue(
            object : Callback<List<DoctorData>> {
            override fun onResponse(
                call: Call<List<DoctorData>>,
                response: Response<List<DoctorData>>
            ) {
                val doctors = response.body()
                onResult(doctors)
            }
                override fun onFailure(call: Call<List<DoctorData>>, t: Throwable) {
                    onResult(null)
                }
            })
    }
}