package com.epaymark.big9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.databinding.BrowserListBinding
import com.epaymark.big9.data.model.BrowserModel

import com.epaymark.big9.utils.`interface`.CallBack

class BrowserAdapter(var operatorList: ArrayList<BrowserModel>, val listner: CallBack) : RecyclerView.Adapter<BrowserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = BrowserListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(operatorList[position], position)
    }

    override fun getItemCount(): Int {
        return operatorList.size
    }


    inner class MyViewHolder(val binding: BrowserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BrowserModel, position: Int) {
            binding.model=item
            binding.cardView.setOnClickListener{
                listner.getValue(item.amt)
            }
            ///*if (position!=items.size-1) {

            //}*/
           /* item.title.let {
              //  binding.tvState.text = it
            }

            binding.clHeader.setOnClickListener{
                item.title.let {
                    listner.getValue(it)
                }

            }*/
            binding.executePendingBindings()
        }
    }


}