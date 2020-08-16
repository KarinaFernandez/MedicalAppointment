package com.example.reserves.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reserves.entities.DoctorData

@Dao
interface DoctorDao {

    @Query("SELECT * FROM DoctorData")
    //fun getDoctors(): LiveData<List<DoctorData>>
    fun getDoctors(): List<DoctorData>
}