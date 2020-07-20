package com.example.reserves

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*

import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {
    //, DoctorAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val list = ArrayList<DoctorItem>()
        val apiService = RestApiService()
        apiService.getDoctors() {
            if (it != null) {
                val doctorList = it

                doctorList.forEach {
                    val item = DoctorItem(it.nombre,
                        it.apellido
                    )
                    list += item
                }
                doctorsRecyclerView.adapter = DoctorAdapter(list )
                doctorsRecyclerView.layoutManager = LinearLayoutManager(this)
                doctorsRecyclerView.setHasFixedSize(true)
            } else {
                Toast.makeText(
                    applicationContext,
                    "No se pudo obtener la lista de doctores",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /*
    override fun onItemClick(position: Int) {
        val clickedDoctor : DoctorItem = list
    }*/
}