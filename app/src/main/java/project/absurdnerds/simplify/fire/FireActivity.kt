package project.absurdnerds.simplify.fire

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_fire.*
import project.absurdnerds.simplify.LocationChangeInterface
import project.absurdnerds.simplify.MapsFragment
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.data.request.FireRequest
import project.absurdnerds.simplify.data.response.FirePostResponse
import project.absurdnerds.simplify.databinding.ActivityFireBinding
import project.absurdnerds.simplify.fire.FireViewState.*
import project.absurdnerds.simplify.utils.AppConfig.SHARED_PREF
import project.absurdnerds.simplify.utils.allPermissionsGranted
import project.absurdnerds.simplify.utils.dialog.ViewDialog
import project.absurdnerds.simplify.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class FireActivity : AppCompatActivity(), LocationChangeInterface {

    companion object{
        private lateinit var firebaseAuth: FirebaseAuth
        private var mobileNumber: String = ""
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var sweetAlertDialog: SweetAlertDialog
        private val REQUEST_CODE_PERMISSIONS = 99
        private var location: String = ""
        private var latLong: String = ""
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        fun start(context: Context) {
            val intent = Intent(context, FireActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityFireBinding

    private lateinit var viewModel : FireViewModel

    private lateinit var loadingDialog : ViewDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fire)
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

        setSupportActionBar(toolFire)
//        toolFire.inflateMenu(R.menu.menu)

        firebaseAuth = FirebaseAuth.getInstance()
        initViewModel()
        setObservers()
        initUI()

        if (sharedPreferences.contains("PHONE")) {
            mobileNumber = sharedPreferences.getString("PHONE", "0").toString()
        }
        else {
            mobileNumber = firebaseAuth.currentUser!!.phoneNumber.toString()
        }



        supportFragmentManager.beginTransaction()
            .replace(R.id.locationFrame, MapsFragment())
            .commit()

        btAlert.setOnClickListener {

            showToast("Alert")
            reportFire()

        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(FireViewModel::class.java)
        loadingDialog = ViewDialog(this)
    }

    private fun initUI() {
        binding.viewModel = viewModel
    }

    private fun setObservers() {
        viewModel.stateObservable.observe(this, {
            render(it)
        })
    }

    private fun render(state: FireViewState) {
        when(state) {
            Loading -> showLoading()
            OnSuccess -> onSuccess()
            is OnError -> onError(state.mssg)
            is FieldError -> showFieldError(state)
        }
    }

    private fun showLoading() {
        loadingDialog.showDialog()
    }

    private fun hideLoading() {
        loadingDialog.hideDialog()
    }

    private fun onSuccess() {
        hideLoading()
        showToast("Success")
    }

    private fun onError(message: String?) {
        hideLoading()
        if (message != null) {
            showToast(message)
        }
    }

    private fun showFieldError(type: FieldError) {
        hideLoading()
        /*when (type.errorType) {
            ErrorField.FIRST_NAME -> binding.etFirstName.error = type.message
            ErrorField.LAST_NAME -> binding.etLastName.error = type.message
            ErrorField.DOB -> binding.etDob.error = type.message
            ErrorField.HEIGHT -> binding.etHeight.error = type.message
            ErrorField.WEIGHT -> binding.etWeight.error = type.message
            ErrorField.PHONE -> binding.etPhone.error = type.message
            ErrorField.GENDER -> showToast(type.message)
        }*/
    }

    private fun reportFire() {

        sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = Color.parseColor("#A5DC86");
        sweetAlertDialog.titleText = "Loading";
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        location = etReportFireLocation.text.toString()
        var apiInterface = ApiInterface.invoke()
        val postRequest = FireRequest(latLong, location, mobileNumber)
        var call: Call<FirePostResponse> = apiInterface.postFireData(postRequest)

        call.enqueue(object : Callback<FirePostResponse> {
            override fun onResponse(
                call: Call<FirePostResponse>,
                response: Response<FirePostResponse>
            ) {
                Timber.e("Fire Report : ${response.code().toString()}")
                if (response.isSuccessful) {
                    var sad = SweetAlertDialog(this@FireActivity, SweetAlertDialog.SUCCESS_TYPE)
                    sad.titleText = "You have Reported Fire in your Area"
                    sad.show()
                } else {
                    showToast("Something went wrong")
                }
                sweetAlertDialog.cancel()
            }

            override fun onFailure(call: Call<FirePostResponse>, t: Throwable) {
                Timber.e(t)
                showToast("Something went wrong")
                sweetAlertDialog.cancel()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted(this))  {
                showToast("Permissions not granted by the user.")
            }
        }
    }

    override fun onLocationChange(a: String, b: String) {

        latLong = b

        etReportFireLocation.setText(a)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.itemHistory -> {
                // TODO Add your Activity to open here
                // INTENT

                showToast("History")

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}