package project.absurdnerds.simplify.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.data.response.AmbulanceGetResponse
import project.absurdnerds.simplify.data.response.PoliceGetResponse
import project.absurdnerds.simplify.databinding.ItemHistoryPoliceBinding

class PoliceAdapter constructor(
    private  val list : List<PoliceGetResponse.PoliceGetResponseItem>,
    private val context : Context
) : RecyclerView.Adapter<PoliceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliceAdapter.ViewHolder =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_history_police,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PoliceAdapter.ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        private val binding : ItemHistoryPoliceBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind (item : PoliceGetResponse.PoliceGetResponseItem) {

            binding.tvName.text = item.problem
            binding.tvLocation.text = item.address
            binding.tvDate.text = item.created

        }

    }
}