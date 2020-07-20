package com.example.reserves

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*

import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), DoctorAdapter.OnItemClickListener, SearchView.OnQueryTextListener {
    private var list = ArrayList<DoctorItem>()
    private var doctors = ArrayList<DoctorItem>()
    private val adapter = DoctorAdapter(doctors, this)
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(this)

        recyclerView = findViewById(R.id.doctorsRecyclerView)

        // Get doctors from API
        getDoctors()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(s: String): Boolean {
        adapter.filter.filter(s)
        return false
    }

    private fun getDoctors() {
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
                    list.plusAssign(item)
                    doctors = list
                }

                recyclerView.adapter = DoctorAdapter(doctors, this)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.setHasFixedSize(true)
            } else {
                Toast.makeText(
                    applicationContext,
                    "No se pudo obtener la lista de doctores",
                    Toast.LENGTH_LONG
                ).show()
            }
            progress_bar.visibility = View.GONE
        }
    }

    override fun onItemClick(item: DoctorItem, position: Int) {
        val selectedDoctor = doctors[position]
        adapter.notifyItemChanged(position)

        // Push to schedule activity
        val intent = Intent(this, ScheduleActivity::class.java)
        intent.putExtra("doctorId", selectedDoctor.id)
        intent.putExtra("doctorName", selectedDoctor.name)
        intent.putExtra("doctorSurname", selectedDoctor.surname)
        startActivity(intent)
    }
}