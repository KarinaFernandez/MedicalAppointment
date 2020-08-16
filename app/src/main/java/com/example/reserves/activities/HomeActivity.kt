package com.example.reserves.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reserves.DoctorAdapter
import com.example.reserves.DoctorItem
import com.example.reserves.R
import com.example.reserves.daos.DoctorDao
import com.example.reserves.dataBase.AppDatabase
import com.example.reserves.entities.DoctorData
import com.example.reserves.network.RestApiService
import com.example.reserves.repositories.Repository
import io.reactivex.Completable.fromCallable
import io.reactivex.Observable.fromCallable
import kotlinx.android.synthetic.main.activity_home.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), DoctorAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

  //  private val repository: Repository
  //  val allDoctors: LiveData<List<DoctorData>>

    private var db: AppDatabase? = null
    private var doctorDao: DoctorDao? = null

    private var list = ArrayList<DoctorItem>()
    private var doctors = ArrayList<DoctorItem>()
    private val adapter = DoctorAdapter(doctors, this)
    lateinit var recyclerView: RecyclerView

    /*
    init {
        val doctorsDao = AppDatabase.getAppDataBase(application)?.doctorDao()
        repository = doctorsDao?.let { Repository(it) }!!
        allDoctors = repository.allDoctors
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(this)

        recyclerView = findViewById(R.id.doctorsRecyclerView)

        // Get doctors from API
        getDoctors()

        // get doctors from DB
        getDoctorsFromDB()
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

                recyclerView.adapter =
                    DoctorAdapter(doctors, this)
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

    private fun getDoctorsFromDB() {
        db = AppDatabase.getAppDataBase(context = this)

        Observable.fromCallable {
            db = AppDatabase.getAppDataBase(context = this)
            doctorDao = db?.doctorDao()

            db?.doctorDao()?.getDoctors()
        }.doOnNext { list ->
            var finalString = ""
            // list?.map { finalString += it.name + " - " }
            // tv_message.text = finalString

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
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