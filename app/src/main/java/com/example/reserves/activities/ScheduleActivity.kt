package com.example.reserves.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reserves.R
import com.example.reserves.entities.ScheduleData
import com.example.reserves.network.RestApiService
import java.text.SimpleDateFormat
import java.util.*
import android.annotation.SuppressLint
import android.location.LocationListener

private const val PERMISSION_REQUEST = 10

class ScheduleActivity : AppCompatActivity()  {

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    var latitude: Double? = null
    var longitude: Double? = null

    private var permission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        // Get values from Home Activity
        val doctorId=intent.getStringExtra("doctorId")
        val doctorName=intent.getStringExtra("doctorName")
        val doctorSurname=intent.getStringExtra("doctorSurname")

        val scheduleDoctor = findViewById<TextView>(R.id.scheduleDoctor)
        val doctorInfo = findViewById<TextView>(R.id.doctorInfo)
        val date = findViewById<TextView>(R.id.date)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val scheduleButton = findViewById<Button>(R.id.appointmentButton)

        // Load schedule info
        scheduleDoctor.text = getString(R.string.scheduleInfo)
        doctorInfo.text = StringBuilder().append(doctorName).append(" ").append(doctorSurname).toString()

        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm")
        val currentDate = dateFormat.format(Date())
        date.text = currentDate

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val month = month + 1
            val selectedDay = "$month-$day-$year 07:00"
            date.text = selectedDay
        }

        scheduleButton.setOnClickListener {
            scheduleButton.isClickable = false
            val sharedPreferences = getSharedPreferences("User_Info", Context.MODE_PRIVATE)
            val userId =  sharedPreferences.getString("userId", "")

            val apiService = RestApiService()
            val schedule = ScheduleData(
                _id = null,
                medico = doctorId,
                usuario = userId,
                fecha = date.text.toString(),
                latitud = latitude,
                longitud = longitude
            )

            apiService.createSchedule(schedule) {
                if (it?._id != null) {
                    /*
                     Print schedule id to check saved coords
                     https://obligatorio-169189.herokuapp.com/reservas/id
                     */
                    print(it?._id)

                    Toast.makeText(
                        applicationContext,
                        getString(R.string.scheduleSuccessMessage),
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.scheduleFailedMessage),
                        Toast.LENGTH_LONG
                    ).show()
                }
                //progress_bar.visibility = View.GONE
                scheduleButton.isClickable = true
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Check location permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permission)) {
                getLocation()
            } else {
                requestPermissions(permission, PERMISSION_REQUEST)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {
            if (hasGps) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location

                            latitude = locationNetwork?.latitude
                            longitude = locationNetwork?.longitude

                        } else {
                            print("No coords")
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }
                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation

            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location

                            latitude = locationNetwork?.latitude
                            longitude = locationNetwork?.longitude
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                    latitude = locationNetwork?.latitude
                    longitude = locationNetwork?.longitude

                } else {
                    latitude = locationNetwork?.latitude
                    longitude = locationNetwork?.longitude
                }
            }

        } else {
            Toast.makeText(this, getString(R.string.coordsErrorMessage), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED) {
                allSuccess = false
            }
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array <out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, getString(R.string.locationPermisionDenied), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}