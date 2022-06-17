package com.example.storyapp.view.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.storyapp.R
import com.example.storyapp.data.Fetch
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.model.SystemPreferences
import com.example.storyapp.model.UserModel
import com.example.storyapp.view.StoryModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var userModel: UserModel
    private lateinit var mSystemPreferences: SystemPreferences
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val viewModel: MapsViewModel by viewModels{
        StoryModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSystemPreferences = SystemPreferences(this@MapsActivity)
        userModel = mSystemPreferences.getUser()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val indonesia = LatLng(0.7893, 113.9123)

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 4f))

        getLocation()
        setMapStyle()
        getMyLocation()
    }

    private fun getLocation(){
        viewModel.getLocation("Bearer " + userModel.token.toString()).observe(this){ result ->
            if (result != null) {
                when (result) {
                    is Fetch.Loading -> {
                        Log.i("FETCH", "LOADING")
                    }
                    is Fetch.Success -> {
                        val data = result.data
                        if (data.isEmpty()){
                            Toast.makeText(
                                this,
                                "Location " + getString(R.string.empty),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        setLocation(data)
                    }
                    is Fetch.Error -> {
//                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            result.error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun setLocation(location: List<ListStoryItem>){
        location.forEachIndexed { _, item ->
            mMap.addMarker(MarkerOptions().position(LatLng(item.lat, item.lon)).title(item.name).snippet(item.description))
        }
    }

    private fun setMapStyle(){
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    Log.i("CURRENTLOC", "${location.latitude}, ${location.longitude}")
                }

            }

    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}
