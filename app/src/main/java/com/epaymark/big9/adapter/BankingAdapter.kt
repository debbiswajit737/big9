package com.epaymark.big9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.databinding.UpiLayoutBinding
import com.epaymark.big9.data.model.ListIcon

import com.epaymark.epay.utils.`interface`.CallBack


class BankingAdapter(private val items: List<ListIcon>, val circleShape: Int, val callback: CallBack) : RecyclerView.Adapter<BankingAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {



        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: UpiLayoutBinding = UpiLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: UpiLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListIcon, position: Int) {
            ///*if (position!=items.size-1) {
                binding.imgIcon.setBackgroundResource(circleShape)
            //}*/
            item.title?.let {item->
                binding.tvTitle.text = item
                binding.llContainer.setOnClickListener{
                    callback.getValue(item)
                }
            }
            item.image?.let {image->
                binding.imgIcon.setImageResource(image)
            }


        }
    }


}