package weatherstations.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationDataSource (
    private val context: Context
) {

    private val _lastLocation = MutableStateFlow<Location?>(null)
    val lastLocation: StateFlow<Location?>
        get() = _lastLocation

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean>
        get() = _isConnected

    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted: StateFlow<Boolean>
        get() = _isPermissionGranted


    private fun updateConnectionStatus() {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        _isConnected.value = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun updatePermissionStatus(value: Boolean) {
        _isPermissionGranted.value = value
    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        if (!_isPermissionGranted.value) {
            return
        }

        updateConnectionStatus()

        val client = LocationServices.getFusedLocationProviderClient(context)
        client.lastLocation
            .addOnSuccessListener { location ->
                _lastLocation.value = location
            }
            .addOnFailureListener { exception ->
            }
    }
}