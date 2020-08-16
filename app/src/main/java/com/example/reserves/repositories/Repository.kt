package com.example.reserves.repositories

import androidx.lifecycle.LiveData
import com.example.reserves.daos.DoctorDao
import com.example.reserves.entities.DoctorData

class Repository(private val doctorDao: DoctorDao) {

    // Observed LiveData will notify the observer when the data has changed.
   // val allDoctors: LiveData<List<DoctorData>> = doctorDao.getDoctors()
}