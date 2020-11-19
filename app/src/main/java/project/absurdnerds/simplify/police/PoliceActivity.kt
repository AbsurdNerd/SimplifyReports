package project.absurdnerds.simplify.police

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.data.PoliceModel
import project.absurdnerds.simplify.databinding.ActivityPoliceBinding
import project.absurdnerds.simplify.police.adapter.PoliceReportAdapter
import project.absurdnerds.simplify.police.policeReport.PoliceReportActivity
import project.absurdnerds.simplify.utils.showToast

class PoliceActivity : AppCompatActivity() {

    companion object{
        fun start(context: Context) {
            val intent = Intent(context, PoliceActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityPoliceBinding

    private lateinit var adapter : PoliceReportAdapter

    private var list : MutableList<PoliceModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_police)
        initAdapter()
        fillList()
    }

    private fun initAdapter() {
        adapter = PoliceReportAdapter(list, this)
        binding.recycleReportType.adapter = adapter
    }

    private fun fillList(){
        list.add(PoliceModel(getString(R.string.found),false))
        list.add(PoliceModel(getString(R.string.missing),true))
        list.add(PoliceModel(getString(R.string.death),false))
        list.add(PoliceModel(getString(R.string.robbery),true))
        adapter.notifyDataSetChanged()
    }

    fun onClick(model: PoliceModel) {
        PoliceReportActivity.start(
                this,
                model.reportType,
                model.notify
        )
    }
}