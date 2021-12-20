package com.example.pomeloassignment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pomeloassignment.adapters.ListAdapter
import com.example.pomeloassignment.databinding.ActivityMainBinding
import com.example.pomeloassignment.source.DataSource
import com.example.pomeloassignment.source.RemoteDataSource
import com.example.pomeloassignment.viewmodels.MainActivityViewModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    private var currentLocation:Location?=null

    private lateinit var viewModel: MainActivityViewModel
    private var isFirstTime=true
    private val listAdapter = ListAdapter(arrayListOf())
    companion object {
        private const val PERMISSION_ID = 44
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel =
            ViewModelProvider(this, MainActivityModelFactory(RemoteDataSource(),Dispatchers.IO)).get(
                MainActivityViewModel::class.java
            )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
            itemAnimator=null
        }
        viewModel.getPickUpsList()
        observeViewModel()
        binding.getLocationButton.setOnClickListener {
            currentLocation=null
           getLastLocation()
        }

    }

    private fun observeViewModel() {
        viewModel.getPickUpsLiveData().observe(this, Observer { it ->
            when (it.status){
                Status.SUCCESS->{
                    binding.progressBar.visibility= View.GONE
                    it?.let {
                        listAdapter.updateList(it?.data!!) }
                }Status.LOADING->{
                binding.progressBar.visibility= View.VISIBLE
            }Status.ERROR->{
                binding.progressBar.visibility= View.GONE
                Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
            }
            }

        })
    }
    class  MainActivityModelFactory(private val dataSource: DataSource,private val context: CoroutineContext) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel( dataSource,context) as T
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.lastLocation
                    .addOnCompleteListener(OnCompleteListener<Location?> { task ->
                        val location = task.result
                        currentLocation=location
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            viewModel.sortList(location.latitude,location.longitude,Dispatchers.Default)
                        }
                    })
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if(currentLocation==null){
                currentLocation = locationResult.lastLocation
                viewModel.sortList(currentLocation!!.latitude,currentLocation!!.longitude,Dispatchers.Default)

            }
        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(checkPermissions() && isLocationEnabled() && !isFirstTime){
            getLastLocation()
        }
        if(isFirstTime){
            isFirstTime=false
        }
    }


}