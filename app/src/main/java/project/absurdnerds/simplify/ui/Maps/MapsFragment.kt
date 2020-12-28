package project.absurdnerds.simplify.Maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.utils.REQUEST_CODE_PERMISSIONS
import project.absurdnerds.simplify.utils.REQUIRED_PERMISSIONS
import project.absurdnerds.simplify.utils.allPermissionsGranted


class MapsFragment : Fragment() {

    private val DEFAULT_ZOOM = 18f
    private var addressList: List<Address>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        var locationChangeInterface = context as LocationChangeInterface

        if (allPermissionsGranted(context!!) && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->

            val callback = OnMapReadyCallback { googleMap ->

                var myLocation = LatLng(
                    location!!.latitude,
                    location!!.longitude
                )
//                googleMap.addMarker(
//                    MarkerOptions().position(myLocation).title("Your current position")
//                )

                googleMap.setOnCameraIdleListener {
                    myLocation = LatLng(
                        googleMap.cameraPosition.target.latitude,
                        googleMap.cameraPosition.target.longitude
                    )
                    val geocoder = Geocoder(context)
                    var address =
                        geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)

                    var area = ""
                    if (!address[0].getAddressLine(0).isNullOrEmpty()) {
                        area += "${address[0].getAddressLine(0).toString()} "
                    }
                    else {
                        if (!address[0].subLocality.isNullOrEmpty()) {
                            area += "${address[0].subLocality.toString()} "
                        }

                        if (!address[0].adminArea.isNullOrEmpty()) {
                            area += "${address[0].adminArea.toString()} "
                        }

                        if (!address[0].adminArea.isNullOrEmpty()) {
                            area += "${address[0].adminArea.toString()} "
                        }
                    }

                    locationChangeInterface.onLocationChange(
                        area,
                        "${ myLocation.latitude},${myLocation.longitude}"
                    )

                }


                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled = true;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, DEFAULT_ZOOM))
            }


            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

    }
}