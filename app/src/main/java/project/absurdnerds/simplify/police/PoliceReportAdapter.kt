package project.absurdnerds.simplify.police

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.databinding.ItemPoliceReportBinding

class PoliceReportAdapter constructor(
        private  val list : List<String>,
        private val context : Context
) : RecyclerView.Adapter<PoliceReportAdapter.ReportHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliceReportAdapter.ReportHolder =
            ReportHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_police_report,
                        parent,
                        false
                )
            )

    override fun onBindViewHolder(holder: PoliceReportAdapter.ReportHolder, position: Int) {
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

        fun bind (item : String) {

            binding.tvReportName.text = item

        }

    }

}