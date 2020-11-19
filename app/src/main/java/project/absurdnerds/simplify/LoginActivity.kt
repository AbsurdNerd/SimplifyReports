package project.absurdnerds.simplify

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import project.absurdnerds.simplify.Fragment.LoginMobileFragment
import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.data.request.ProfilePutRequest
import project.absurdnerds.simplify.home.HomeActivity
import project.absurdnerds.simplify.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoginActivity : AppCompatActivity(), FragmentChangeInterface {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sweetAlertDialog: SweetAlertDialog
    private val REQUEST_CODE_PERMISSIONS = 99
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var fragment = LoginMobileFragment()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREF), Context.MODE_PRIVATE)

        if (firebaseAuth.currentUser != null) {
            sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            sweetAlertDialog.progressHelper.barColor = Color.parseColor(R.color.progressBarColor.toString())
            sweetAlertDialog.titleText = getString(R.string.loading)
            sweetAlertDialog.setCancelable(false)
            sweetAlertDialog.show()

            sharedPreferences.edit().putString(getString(R.string.PHONE), firebaseAuth.currentUser!!.phoneNumber)
                .apply()

            Timber.e(firebaseAuth.currentUser!!.phoneNumber)
            createToken(firebaseAuth.currentUser!!.phoneNumber.toString())

        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.loginFrame, fragment)
        fragmentTransaction.commit()

    }

    override fun changeFragment(fragment: Fragment) {
        if (supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.loginFrame, fragment).commit()
        }
        else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginFrame, fragment).commit()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun createToken(mobileNumber: String) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            checkUser(mobileNumber, it)
        }
    }

    private fun checkUser(mobile: String, token: String) {
        var apiInterface = ApiInterface.invoke()
        val commonPostResponse = ProfilePutRequest(mobile, token)
        var call: Call<Void> = apiInterface.putProfileToken(commonPostResponse)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                Timber.e("860 : ${response.code().toString()}")
                if (response.code() == 200) {
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                }
                else {
                    startActivity(Intent(this@LoginActivity, NewUserActivity::class.java))
                }
                sweetAlertDialog.cancel()
               finish()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.e(t)
                sweetAlertDialog.cancel()
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted())  {
                showToast(getString(R.string.permissions_not_granted_by_the_user))
            }
        }
    }

}