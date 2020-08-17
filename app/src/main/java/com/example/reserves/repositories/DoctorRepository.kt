package com.example.reserves.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reserves.adapters.DoctorItem
import com.example.reserves.daos.DoctorDao
import com.example.reserves.dataBase.AppDatabase
import com.example.reserves.entities.DoctorData

class DoctorRepository(private val doctorDao: DoctorDao) {

    val allDoctors: LiveData<List<DoctorItem>> = doctorDao.getDoctors()

    suspend fun insert(doctor: DoctorData) {
        doctorDao.insert(doctor)
    }

    fun getDoctors():LiveData<List<DoctorItem>> {
        return allDoctors
    }
}