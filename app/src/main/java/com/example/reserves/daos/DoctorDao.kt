package com.example.reserves.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reserves.adapters.DoctorItem
import com.example.reserves.entities.DoctorData

@Dao
interface DoctorDao {

    // fun getDoctors(): List<DoctorData>

    @Query("SELECT * FROM DoctorData")
    fun getDoctors(): LiveData<List<DoctorItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(doctor: DoctorData)

    @Query("DELETE FROM DoctorData")
    suspend fun deleteAll()
}