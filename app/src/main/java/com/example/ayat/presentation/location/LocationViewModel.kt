package com.example.ayat.presentation.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.AyatApplication
import com.example.ayat.data.repositories.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val locationRepository : LocationRepository
) : ViewModel() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    @SuppressLint("MissingPermission")
     fun getCurrentLocation(
        onGetCurrentLocationSuccess: () -> Unit,
        onGetLastLocationIsNull:()->Unit,
        onGetCurrentLocationFailed:() -> Unit,

        priority: Boolean = true
    ) {
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    if (areLocationPermissionsGranted()) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AyatApplication.getApplicationContext())
        fusedLocationProviderClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                viewModelScope.launch(Dispatchers.IO){
                    locationRepository.saveLocation(latitude = it.latitude.toString(), longitude = it.latitude.toString())
                        withContext(Dispatchers.Main){
                            onGetCurrentLocationSuccess()

                        }


                }
            }?.run {
                onGetLastLocationIsNull()

            }
        }.addOnFailureListener {
           onGetCurrentLocationFailed()
        }
    }


    }

    @SuppressLint("MissingPermission")
    fun getLastUserLocation(
        onGetLastLocationSuccess: ()->Unit,
        onGetLastLocationFailed: (Exception) -> Unit
    ) {
            if (areLocationPermissionsGranted()) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AyatApplication.getApplicationContext())
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            viewModelScope.launch(Dispatchers.IO){
                                locationRepository.saveLocation(latitude = it.latitude.toString(), longitude = it.latitude.toString())
                                   viewModelScope.launch(Dispatchers.Main){
                                       onGetLastLocationSuccess()

                                   }

                            }

                        }
                    }
                    .addOnFailureListener { exception ->
                        onGetLastLocationFailed(exception)
                    }
            }


    }
    private fun areLocationPermissionsGranted(): Boolean {

        return (ActivityCompat.checkSelfPermission(
            AyatApplication.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    AyatApplication.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }
}
