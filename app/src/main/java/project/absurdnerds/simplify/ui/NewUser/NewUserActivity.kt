package project.absurdnerds.simplify.NewUser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_new_user.*
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.data.request.ProfileRequest
import project.absurdnerds.simplify.data.response.ProfilePostResponse
import project.absurdnerds.simplify.ui.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class NewUserActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentLocation: String
    private lateinit var sweetAlertDialog: SweetAlertDialog
    private val REQUEST_CODE_PERMISSIONS = 99
    private val REQUEST_CODE_LOCATION = 98
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        firebaseAuth = FirebaseAuth.getInstance()

        etPhone.setText(firebaseAuth.currentUser!!.phoneNumber.toString())

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)




        cardSubmit.setOnClickListener {

            Timber.e("Click")
            if (allPermissionsGranted()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            // Got last known location. In some rare situations this can be null.
                            currentLocation = "${location!!.latitude},${location.longitude}"

                            if (checkFields()) {
                                FirebaseMessaging.getInstance().token.addOnSuccessListener {
                                    createUser(it)
                                }
                            }
                            else {
                                Toast.makeText(this, getString(R.string.all_fields_are_compulsory), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else {
                    Toast.makeText(this, getString(R.string.turn_on_location_access_from_settings), Toast.LENGTH_SHORT).show()
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    private fun checkFields(): Boolean {
        if (etFName.text.isNullOrEmpty())
            return false
        if (etLName.text.isNullOrEmpty())
            return false
        if (etPhone.text.isNullOrEmpty())
            return false
        if (etPermanentAddress.text.isNullOrEmpty())
            return false
        return true
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createUser(token: String) {

        sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = Color.parseColor(R.color.progressBarColor.toString())
        sweetAlertDialog.titleText = R.string.loading.toString()
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.show()

        var gender = "Male"

        gender = if (rbMale.isChecked)
            "Male"
        else
            "Female"


        var apiInterface = ApiInterface.invoke()
        val profilePostRequest = ProfileRequest(
            "${etFName.text.toString()} ${etLName.text.toString()}",
            "${etPhone.text.toString()}",
            gender,
            currentLocation,
            "${etPermanentAddress.text.toString()}",
            token

        )
        var call: Call<ProfilePostResponse> = apiInterface.postProfileData(profilePostRequest)

        call.enqueue(object : Callback<ProfilePostResponse> {
            override fun onResponse(
                call: Call<ProfilePostResponse>,
                response: Response<ProfilePostResponse>
            ) {
                if (response.isSuccessful) {
                    startActivity(Intent(this@NewUserActivity, HomeActivity::class.java))
                    finish()
                }
                sweetAlertDialog.cancel()
            }

            override fun onFailure(call: Call<ProfilePostResponse>, t: Throwable) {
                Timber.e(t)
                sweetAlertDialog.cancel()
            }

        })
    }
}