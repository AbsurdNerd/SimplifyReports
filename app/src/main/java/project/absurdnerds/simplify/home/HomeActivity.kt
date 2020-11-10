package project.absurdnerds.simplify.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.databinding.ActivityHomeBinding
import project.absurdnerds.simplify.fire.FireActivity
import project.absurdnerds.simplify.medical.MedicalActivity
import project.absurdnerds.simplify.police.PoliceActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initView()
        onClick()
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
}