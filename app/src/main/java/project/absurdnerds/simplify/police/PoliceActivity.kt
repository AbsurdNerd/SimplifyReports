package project.absurdnerds.simplify.police

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.databinding.ActivityPoliceBinding

class PoliceActivity : AppCompatActivity() {

    companion object{
        fun start(context: Context) {
            val intent = Intent(context, PoliceActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityPoliceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_police)
    }
}