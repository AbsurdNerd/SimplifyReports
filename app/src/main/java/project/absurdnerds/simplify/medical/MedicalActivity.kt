package project.absurdnerds.simplify.medical

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_fire.*
import kotlinx.android.synthetic.main.activity_medical.*
import project.absurdnerds.simplify.LocationChangeInterface
import project.absurdnerds.simplify.MapsFragment
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.data.request.AmbulancePostRequest
import project.absurdnerds.simplify.data.request.AmbulanceRequest
import project.absurdnerds.simplify.data.request.FireRequest
import project.absurdnerds.simplify.data.response.FirePostResponse
import project.absurdnerds.simplify.databinding.ActivityMedicalBinding
import project.absurdnerds.simplify.fire.FireActivity
import project.absurdnerds.simplify.medical.MedicalViewState.*
import project.absurdnerds.simplify.utils.AppConfig.SHARED_PREF
import project.absurdnerds.simplify.utils.dialog.ViewDialog
import project.absurdnerds.simplify.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class MedicalActivity : AppCompatActivity(), LocationChangeInterface {

    companion object{

        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var sweetAlertDialog: SweetAlertDialog
        private var location: String = ""
        private var latLong: String = ""
        private var mobileNumber: String = ""

        fun start(context: Context) {
            val intent = Intent(context, MedicalActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityMedicalBinding

    private lateinit var viewModel : MedicalViewModel

    private lateinit var loadingDialog : ViewDialog

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical)
        initViewModel()
        setObservers()
        initUI()

        setSupportActionBar(toolMedical)

        medicalLocationCard.setOnTouchListener(OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> medicalScroll.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> medicalScroll.requestDisallowInterceptTouchEvent(
                    false
                )
            }
            medicalLocationCard.onTouchEvent(event)
        })

        patientLocationFrame.setOnTouchListener(OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> medicalScroll.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> medicalScroll.requestDisallowInterceptTouchEvent(
                    false
                )
            }
            medicalLocationCard.onTouchEvent(event)
        })

        btAmbulanceCall.setOnClickListener {
            reportPatient()
        }

    }

    private fun reportPatient() {

        sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = Color.parseColor(R.color.progressBarColor.toString())
        sweetAlertDialog.titleText = R.string.loading.toString()
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.show()

        location = etPatientAddress.text.toString()
        var gender = spPatientGender.selectedItem.toString()
        var blood = spPatientBloodGroup.selectedItem.toString()
//        var gender = "Male"
//        var blood = "O+"
        var apiInterface = ApiInterface.invoke()
        val postRequest = AmbulancePostRequest(
            etPatientName.text.toString(),
            etPatientAge.text.toString().toInt(),
            etPatientPhone.text.toString(),
            gender,
            blood,
            etPatientProblem.text.toString(),
            latLong,
            etPatientAddress.text.toString(),
            mobileNumber
        )
        Timber.e(postRequest.toString())
        var call: Call<AmbulanceRequest> = apiInterface.postAmbulanceData(postRequest)

        call.enqueue(object : Callback<AmbulanceRequest> {
            override fun onResponse(
                call: Call<AmbulanceRequest>,
                response: Response<AmbulanceRequest>
            ) {
                Timber.e("Medical Report : ${response.code().toString()}")
                if (response.isSuccessful) {
                    var sad = SweetAlertDialog(this@MedicalActivity, SweetAlertDialog.SUCCESS_TYPE)
                    sad.titleText = getString(R.string.you_have_reported_a_patient)
                    sad.show()
                }
                else {
                    showToast(getString(R.string.something_went_wrong))
                }
                sweetAlertDialog.cancel()
            }

            override fun onFailure(call: Call<AmbulanceRequest>, t: Throwable) {
                Timber.e(t)
                showToast(getString(R.string.something_went_wrong))
                sweetAlertDialog.cancel()
            }
        })

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MedicalViewModel::class.java)
        loadingDialog = ViewDialog(this)
    }

    private fun setObservers() {
        viewModel.stateObservable.observe(this, {
            render(it)
        })
    }

    private fun initUI() {

        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.patientLocationFrame, MapsFragment())
            .commit()

        if (sharedPreferences.contains("PHONE")) {
            mobileNumber = sharedPreferences.getString("PHONE", "0").toString()
        }
        else {
            mobileNumber = firebaseAuth.currentUser!!.phoneNumber.toString()
        }

        binding.viewModel = viewModel
    }

    private fun render(state: MedicalViewState) {
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
        showToast(getString(R.string.success))
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

    override fun onLocationChange(loc: String, lat: String) {

        location = loc
        latLong = lat
        etPatientAddress.setText(location)

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

                showToast(getString(R.string.history))

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}