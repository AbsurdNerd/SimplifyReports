package project.absurdnerds.simplify.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.api.ApiInterface
import project.absurdnerds.simplify.data.request.CommonPhoneRequest
import project.absurdnerds.simplify.data.request.ProfilePutRequest
import project.absurdnerds.simplify.data.response.PolicePostResponse
import project.absurdnerds.simplify.data.response.ProfilePostResponse
import project.absurdnerds.simplify.databinding.ActivityHomeBinding
import project.absurdnerds.simplify.fire.FireActivity
import project.absurdnerds.simplify.medical.MedicalActivity
import project.absurdnerds.simplify.police.PoliceActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initView()
        onClick()
        checkUser()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Timber.e(it)
        }

    }

    private fun onClick() {
        binding.cdFire.setOnClickListener {
            FireActivity.start(
                this
            )
        }

        binding.cdMedical.setOnClickListener {
            MedicalActivity.start(
                this
            )
        }

        binding.cdPolice.setOnClickListener {
            PoliceActivity.start(
                this
            )
        }
    }

    private fun initView() {
        Glide.with(this)
                .asGif()
                .load(R.drawable.ambulance)
                .into(binding.imFire)

        Glide.with(this)
                .asGif()
                .load(R.drawable.ambulance)
                .into(binding.imMedical)

        Glide.with(this)
                .asGif()
                .load(R.drawable.ambulance)
                .into(binding.imPolice)
    }

    fun checkUser() {
        var apiInterface = ApiInterface.invoke()
        var map: HashMap<String, Any> = HashMap()
        map["phone"] = "8604609572"

        var map1: HashMap<String, Any> = HashMap()
        map1["phone"] = "9521746567"

        val commonPostResponse = ProfilePutRequest("8604609572", "qwertyuikl;cscsas")
        val commonPostResponse1 = ProfilePutRequest("12345678", "qwertyuiop")
//        var jsonObject: JsonObject = map.toString()
        var call: Call<Void> = apiInterface.putProfileToken(commonPostResponse)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                Timber.e("860 : ${response.code().toString()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.e(t)
            }

        })

        var call1: Call<Void> = apiInterface.putProfileToken(commonPostResponse1)
        call1.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                Timber.e("9521 : ${response.code().toString()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Timber.e(t)
            }

        })

    }
}