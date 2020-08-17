package com.example.reserves.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.reserves.R
import com.example.reserves.entities.DoctorData
import com.example.reserves.entities.ScheduleData
import com.example.reserves.network.RestApiService
import kotlinx.android.synthetic.main.activity_schedule.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import android.net.Uri
import java.util.*

private const val PERMISSION_REQUEST = 10

class ScheduleActivity : AppCompatActivity()  {

    // Location properties
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    var latitude: Double? = null
    var longitude: Double? = null

    // Camera properties
    private var photoFile: File? = null
    private val CAPTURE_IMAGE_REQUEST = 1
    private lateinit var mCurrentPhotoPath: String
    private var imageURL: String? = null
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    private var permission = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        // Get values from Home Activity
        val doctorId=intent.getStringExtra("doctorId")
        val doctorName=intent.getStringExtra("doctorName")
        val doctorSurname=intent.getStringExtra("doctorSurname")
        val doctorImagePath=intent.getStringExtra("doctorImagePath")

        val scheduleDoctor = findViewById<TextView>(R.id.scheduleDoctor)
        val doctorInfo = findViewById<TextView>(R.id.doctorInfo)
        val date = findViewById<TextView>(R.id.date)
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val scheduleButton = findViewById<Button>(R.id.appointmentButton)
        val addImageButton = findViewById<Button>(R.id.addImageButton)
        val drImageView = findViewById<ImageView>(R.id.doctorImageView)

        // Load dr image
        val uri = Uri.parse(doctorImagePath)
        doctorImageView.setImageURI(uri)

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

        // Schedule button pressed
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

        // Add image button pressed
        addImageButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //permission already granted
                    openCamera()
                }
            } else {
                openCamera()
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
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

    // Check location permission
    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED) {
                allSuccess = false
            }
        }
        return allSuccess
    }

    // Validate permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array <out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Location permission
        if (requestCode == PERMISSION_REQUEST) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, getString(R.string.locationPermisionDenied), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (requestCode == 0) { // Camera permission
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            doctorImageView.setImageURI(image_uri)

            imageURL = image_uri.toString()
            updateDoctorImage()
        }
    }

    private fun updateDoctorImage() {
        val apiService = RestApiService()
        val doctorId = intent.getStringExtra("doctorId")

        if (imageURL != null) {
            val doctor = DoctorData(
                id = null,
                _id = doctorId,
                nombre = null,
                apellido = null,
                titulo = null,
                foto = imageURL,
                puntuacion = null,
                centroMedico = null
            )
            apiService.updateDoctor(doctorId, doctor) {
                if (it != null) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.imageUpdated),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.errorUpdateingImage),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}