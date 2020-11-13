package project.absurdnerds.simplify.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
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
import project.absurdnerds.simplify.utils.AppConfig.SHARED_PREF
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initView()
        onClick()

        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Timber.e(it)
        }

        sharedPreferences.edit().putString(getString(R.string.PHONE), firebaseAuth.currentUser!!.phoneNumber)
            .apply()

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
                .load(R.drawable.fire_gif)
                .into(binding.imFire)

        Glide.with(this)
                .asGif()
                .load(R.drawable.ambulance)
                .into(binding.imMedical)

        Glide.with(this)
                .asGif()
                .load(R.drawable.police_gif)
                .into(binding.imPolice)
    }
}