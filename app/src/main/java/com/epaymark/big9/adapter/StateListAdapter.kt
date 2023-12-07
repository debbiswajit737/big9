package com.epaymark.big9.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.epaymark.big9.utils.`interface`.CallBack
import android.widget.Filter
import android.widget.Filterable
import com.epaymark.big9.databinding.StateListBinding
import com.epaymark.big9.data.model.StateCityModel

class StateListAdapter(private var items: List<StateCityModel>, private val callBack: CallBack) :
    RecyclerView.Adapter<StateListAdapter.MyViewHolder>(), Filterable {

    private var filteredItems: List<StateCityModel> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: StateListBinding = StateListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(filteredItems[position],position)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    inner class MyViewHolder(val binding: StateListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StateCityModel, position: Int) {
            //binding.tvState.text = item.stateCity
            binding.model = item
            binding.tvState.setOnClickListener {

                for(item in filteredItems){
                    item.isSelecetd=false
                }
                filteredItems[position].isSelecetd=true
                callBack.getValue(item.stateCity)

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
                        item.stateCity.lowercase().contains(charSequence)
                    }
                }
                val results = FilterResults()
                results.values = filteredItems
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredItems = results.values as List<StateCityModel>
                notifyDataSetChanged()
            }
        }
    }

    // Update the list of items
    fun updateData(newItems: List<StateCityModel>) {
        items = newItems
        filteredItems = newItems
        notifyDataSetChanged()
    }
}