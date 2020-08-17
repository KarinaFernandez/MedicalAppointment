package com.example.reserves.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reserves.adapters.DoctorItem
import com.example.reserves.network.RestApiService

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private var doctorList: MutableLiveData<List<DoctorItem>>? = null

    fun getDoctorsList(): LiveData<List<DoctorItem>>? {
        if (doctorList == null) {
            doctorList = MutableLiveData()
            getDoctorsFromAPI()
        }
        return doctorList
    }

    private fun getDoctorsFromAPI() {
        val doctors: MutableList<DoctorItem> = ArrayList()

        val apiService = RestApiService()
        apiService.getDoctors() {
            if (it != null) {
                val doctorList = it

                doctorList.forEach {
                    val item = DoctorItem(
                        it._id,
                        it.nombre,
                        it.apellido
                    )
                    doctors.add(item)
                }
                // Save doctors in DB
                this.doctorList?.value = doctors
            }
        }
    }

}