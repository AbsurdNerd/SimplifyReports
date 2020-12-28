package project.absurdnerds.simplify.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.data.response.FireGetResponse
import project.absurdnerds.simplify.databinding.ItemHistoryFireBinding

class FireAdapter constructor(
    private  val list : List<FireGetResponse.FireGetResponseItem>,
    private val context : Context
) : RecyclerView.Adapter<FireAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireAdapter.ViewHolder =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_history_fire,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FireAdapter.ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        private val binding : ItemHistoryFireBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind (item : FireGetResponse.FireGetResponseItem) {

            binding.tvLocation.text = item.address
            binding.tvDate.text = item.created

        }

    }

}