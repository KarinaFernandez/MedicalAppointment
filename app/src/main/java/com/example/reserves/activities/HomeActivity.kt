package com.example.reserves.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reserves.adapters.DoctorAdapter
import com.example.reserves.adapters.DoctorItem
import com.example.reserves.R
import com.example.reserves.network.RestApiService
import com.example.reserves.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), DoctorAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    private var doctors = ArrayList<DoctorItem>()
    private val adapter = DoctorAdapter(doctors, this)
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(this)

        recyclerView = findViewById(R.id.doctorsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()

        // Get doctors from DB
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.getDoctorsList()?.observe(this, Observer { doctorsList ->

            doctors = doctorsList as ArrayList<DoctorItem>
            doctorsList?.let {
                // Update the cached copy of in the adapter
                adapter.doctorFilterList = doctorsList
                recyclerView.adapter =
                    DoctorAdapter(doctors, this)
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(s: String): Boolean {
        adapter.filter.filter(s)
        return false
    }

    override fun onItemClick(item: DoctorItem, position: Int) {
        val selectedDoctor = doctors[position]
        adapter.notifyItemChanged(position)

        // Push to schedule activity
        val intent = Intent(this, ScheduleActivity::class.java)
        intent.putExtra("doctorId", selectedDoctor.id)
        intent.putExtra("doctorName", selectedDoctor.name)
        intent.putExtra("doctorSurname", selectedDoctor.surname)
        intent.putExtra("doctorImagePath", selectedDoctor.imagePath)
        startActivity(intent)
    }
}