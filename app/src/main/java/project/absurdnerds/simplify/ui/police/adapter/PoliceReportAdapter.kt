package project.absurdnerds.simplify.ui.police.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.ui.police.PoliceModel
import project.absurdnerds.simplify.databinding.ItemPoliceReportBinding
import project.absurdnerds.simplify.ui.police.PoliceActivity

class PoliceReportAdapter constructor(
    private  val list : List<PoliceModel>,
    private val context : Context
) : RecyclerView.Adapter<PoliceReportAdapter.ReportHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder =
            ReportHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_police_report,
                        parent,
                        false
                )
            )

    override fun onBindViewHolder(holder: ReportHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener{
            (context as PoliceActivity).onClick(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ReportHolder(
            val binding: ItemPoliceReportBinding
    ) : RecyclerView.ViewHolder(
            binding.root
    ) {

        fun bind (item : PoliceModel) {

            binding.tvReportName.text = item.reportType

        }

    }

}