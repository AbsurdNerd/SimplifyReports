package project.absurdnerds.simplify.ui.police.policeReport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_police_report.*
import project.absurdnerds.simplify.Maps.LocationChangeInterface
import project.absurdnerds.simplify.Maps.MapsFragment
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.data.request.PolicePostRequest
import project.absurdnerds.simplify.data.response.PolicePostResponse
import project.absurdnerds.simplify.databinding.ActivityPoliceReportBinding
import project.absurdnerds.simplify.utils.dialog.ViewDialog
import project.absurdnerds.simplify.utils.showToast
import project.absurdnerds.simplify.ui.police.policeReport.PoliceReportViewState.*
import project.absurdnerds.simplify.utils.AppConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class PoliceReportActivity : AppCompatActivity(), LocationChangeInterface {

    companion object{

        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var sweetAlertDialog: SweetAlertDialog
        private var location: String = ""
        private var latLong: String = ""
        private var mobileNumber: String = ""
        
        private const val INTENT_REPORT_NAME = "intent_report_name"
        private const val INTENT_NOTIFY = "intent_notify"
        fun start(context: Context, report_name: String, notify : Boolean) {
            val intent = Intent(context, PoliceReportActivity::class.java)
            intent.putExtra(INTENT_REPORT_NAME, report_name)
            intent.putExtra(INTENT_NOTIFY, notify)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityPoliceReportBinding

    private lateinit var viewModel : PoliceReportViewModel

    private lateinit var loadingDialog : ViewDialog

    private var reportType : String? = null

    private var notify : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_police_report)
        initViewModel()
        handleIntent()
        setObservers()
        initUI()

        setSupportActionBar(toolPolice)

        supportFragmentManager.beginTransaction()
            .replace(R.id.policeLocationFrame, MapsFragment())
            .commit()

        btPoliceSubmit.setOnClickListener {
            postPoliceRequest()
        }

    }

    private fun postPoliceRequest() {
        sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.progressHelper.barColor = Color.parseColor("#A5DC86");
        sweetAlertDialog.titleText = "Loading";
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        location = etPoliceAddress.text.toString()
        var apiInterface = ApiInterface.invoke()
        val postRequest = PolicePostRequest(
            latLong,
            etPoliceAddress.text.toString(),
            intent.getStringExtra(INTENT_REPORT_NAME),
            etPoliceDescription.text.toString(),
            null,
            intent.getBooleanExtra(INTENT_NOTIFY, false),
            mobileNumber
        )
        Timber.e(intent.getStringExtra(INTENT_REPORT_NAME))
        Timber.e(mobileNumber)
        var call: Call<PolicePostResponse> = apiInterface.postPoliceData(postRequest)

        call.enqueue(object : Callback<PolicePostResponse> {
            override fun onResponse(
                call: Call<PolicePostResponse>,
                response: Response<PolicePostResponse>
            ) {
                Timber.e("Police Report : ${response.code().toString()}")
                Timber.e(response.message().toString())
                if (response.isSuccessful) {
                    var sad = SweetAlertDialog(this@PoliceReportActivity, SweetAlertDialog.SUCCESS_TYPE)
                    sad.titleText = "You have Registered your complaint"
                    sad.show()
                }
                else {
                    showToast("Something went wrong")
                }
                sweetAlertDialog.cancel()
            }

            override fun onFailure(call: Call<PolicePostResponse>, t: Throwable) {
                Timber.e(t)
                showToast("Something went wrong")
                sweetAlertDialog.cancel()
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(PoliceReportViewModel::class.java)
        loadingDialog = ViewDialog(this)
    }

    private fun setObservers() {
        viewModel.stateObservable.observe(this, {
            render(it)
        })
    }

    private fun handleIntent() {
        reportType = intent.getStringExtra(INTENT_REPORT_NAME)
        notify = intent.getBooleanExtra(INTENT_NOTIFY, false)
    }

    private fun initUI() {

        sharedPreferences = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()

        if (sharedPreferences.contains("PHONE")) {
            mobileNumber = sharedPreferences.getString("PHONE", "0").toString()
        }
        else {
            mobileNumber = firebaseAuth.currentUser!!.phoneNumber.toString()
        }


        binding.viewModel = viewModel
    }

    private fun render(state : PoliceReportViewState) {
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

    private fun onError(message : String?) {
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
        etPoliceAddress.setText(location)
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