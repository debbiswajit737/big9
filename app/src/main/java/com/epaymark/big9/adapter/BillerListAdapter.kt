package com.epaymark.big9.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.utils.`interface`.CallBack
import android.widget.Filter
import android.widget.Filterable
import com.bumptech.glide.Glide
import com.epaymark.big9.data.model.ElectricModel
import com.epaymark.big9.databinding.ListElectricBillerBinding

class BillerListAdapter(private var items: List<ElectricModel>, private val callBack: CallBack) :
    RecyclerView.Adapter<BillerListAdapter.MyViewHolder>(), Filterable {

    private var filteredItems: List<ElectricModel> = items
//list_electric_biller
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListElectricBillerBinding = ListElectricBillerBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(filteredItems[position],position)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    inner class MyViewHolder(val binding: ListElectricBillerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ElectricModel, position: Int) {
            //binding.tvState.text = item.stateCity
            binding.model = item
            item.image?.let {
                Glide.with(binding.imgElectricBiller.context)
                    .load(it)
                    .into(binding.imgElectricBiller)

            }

            binding.clHeader.setOnClickListener {

                for(item in filteredItems){
                    item.isSelected=false
                }
                filteredItems[position].isSelected=true
                callBack.getValue(item.biller_name)

                binding.executePendingBindings()
                Log.d("TAG_filteredItems", "bind: "+filteredItems)
                notifyDataSetChanged()
            }

            binding.executePendingBindings()
        }
    }

    // Implement filtering
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSequence = constraint.toString().trim().toLowerCase()
                filteredItems = if (charSequence.isEmpty()) {
                    items
                } else {
                    items.filter { item ->
                        item.biller_name.lowercase().contains(charSequence)
                    }
                }
                val results = FilterResults()
                results.values = filteredItems
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredItems = results.values as List<ElectricModel>
                notifyDataSetChanged()
            }
        }
    }

    // Update the list of items
    fun updateData(newItems: List<ElectricModel>) {
        items = newItems
        filteredItems = newItems
        notifyDataSetChanged()
    }
}