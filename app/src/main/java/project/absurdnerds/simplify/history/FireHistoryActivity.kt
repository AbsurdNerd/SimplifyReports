package project.absurdnerds.simplify.history

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.data.request.CommonPhoneRequest
import project.absurdnerds.simplify.data.response.AmbulanceGetResponse
import project.absurdnerds.simplify.data.response.FireGetResponse
import project.absurdnerds.simplify.data.response.PoliceGetResponse
import project.absurdnerds.simplify.databinding.ActivityFireHistoryBinding
import project.absurdnerds.simplify.history.adapter.AmbulanceAdapter
import project.absurdnerds.simplify.history.adapter.FireAdapter
import project.absurdnerds.simplify.history.adapter.PoliceAdapter
import project.absurdnerds.simplify.utils.AppConfig
import project.absurdnerds.simplify.utils.ReportType
import project.absurdnerds.simplify.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class FireHistoryActivity : AppCompatActivity() {

    companion object {
        private lateinit var firebaseAuth: FirebaseAuth
        private var mobileNumber: String = ""
        private lateinit var sharedPreferences: SharedPreferences
        private const val INTENT_HISTORY = "intent_history"
        fun start(context : Context, history : String) {
            val intent = Intent(context, FireHistoryActivity::class.java)
            intent.putExtra(INTENT_HISTORY, history)
            context.startActivity(intent)
        }
    }
    private lateinit var binding : ActivityFireHistoryBinding

    private lateinit var fireAdapter: FireAdapter

    private lateinit var ambulanceAdapter: AmbulanceAdapter

    private lateinit var policeAdapter: PoliceAdapter

    private var fireList : MutableList<FireGetResponse.FireGetResponseItem> = mutableListOf()

    private var ambulanceList : MutableList<AmbulanceGetResponse.AmbulanceGetResponseItem> = mutableListOf()

    private var policeList : MutableList<PoliceGetResponse.PoliceGetResponseItem> = mutableListOf()

    private lateinit var type : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_fire_history)
        sharedPreferences = getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()
        handleIntent()
        initAdapter()
        if (sharedPreferences.contains("PHONE")) {
            mobileNumber = sharedPreferences.getString("PHONE", "0").toString()
        }
        else {
            mobileNumber = firebaseAuth.currentUser!!.phoneNumber.toString()
        }
    }

    private fun handleIntent() {
        type = intent.getStringExtra(INTENT_HISTORY).toString()
    }

    private fun initAdapter() {

        fireAdapter = FireAdapter(fireList, this)
        ambulanceAdapter = AmbulanceAdapter(ambulanceList, this)
        policeAdapter = PoliceAdapter(policeList, this)

        when(type) {
            ReportType.AMBULANCE.name ->  {
                binding.recycleHistory.adapter = ambulanceAdapter
                fetchAmbulance()
            }
            ReportType.FIRE.name -> {
                binding.recycleHistory.adapter = fireAdapter
                fetchFire()
            }
            else -> {
                binding.recycleHistory.adapter = policeAdapter
                fetchPolice()
            }
        }
    }

    private fun fetchAmbulance() {

        var apiInterface = ApiInterface.invoke()
        val getRequest = CommonPhoneRequest(mobileNumber)
        var call: Call<AmbulanceGetResponse> = apiInterface.getAmbulance(getRequest)

        call.enqueue(object : Callback<AmbulanceGetResponse> {

            override fun onResponse(
                call: Call<AmbulanceGetResponse>,
                response: Response<AmbulanceGetResponse>
            ) {
                Timber.e("ambulance history : ${response.code().toString()}")
                if (response.isSuccessful) {
                    ambulanceList = response.body()!!
                } else {
                    showToast("Something went wrong")
                }
            }

            override fun onFailure(call: Call<AmbulanceGetResponse>, t: Throwable) {
                Timber.e(t)
                showToast("Something went wrong")
            }

        })

        ambulanceAdapter.notifyDataSetChanged()
    }

    private fun fetchFire() {

        var apiInterface = ApiInterface.invoke()
        val getRequest = CommonPhoneRequest(mobileNumber)
        var call: Call<FireGetResponse> = apiInterface.getFire(getRequest)

        call.enqueue(object : Callback<FireGetResponse> {

            override fun onResponse(
                call: Call<FireGetResponse>,
                response: Response<FireGetResponse>
            ) {
                Timber.e("fire history : ${response.code().toString()}")
                if (response.isSuccessful) {
                    fireList = response.body()!!
                } else {
                    showToast("Something went wrong")
                }
            }

            override fun onFailure(call: Call<FireGetResponse>, t: Throwable) {
                Timber.e(t)
                showToast("Something went wrong")
            }

        })

        fireAdapter.notifyDataSetChanged()
    }

    private fun fetchPolice() {

        var apiInterface = ApiInterface.invoke()
        val getRequest = CommonPhoneRequest(mobileNumber)
        var call: Call<PoliceGetResponse> = apiInterface.getPolice(getRequest)

        call.enqueue(object : Callback<PoliceGetResponse> {

            override fun onResponse(
                call: Call<PoliceGetResponse>,
                response: Response<PoliceGetResponse>
            ) {
                Timber.e("police history : ${response.code().toString()}")
                if (response.isSuccessful) {
                    policeList = response.body()!!
                } else {
                    showToast("Something went wrong")
                }
            }

            override fun onFailure(call: Call<PoliceGetResponse>, t: Throwable) {
                Timber.e(t)
                showToast("Something went wrong")
            }

        })

        policeAdapter.notifyDataSetChanged()
    }


}