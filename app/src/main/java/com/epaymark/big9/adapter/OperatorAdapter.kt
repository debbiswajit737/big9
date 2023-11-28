package com.epaymark.big9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epaymark.big9.data.model.OperatorModel
import com.epaymark.big9.databinding.OperatorListBinding
import com.epaymark.big9.utils.`interface`.CallBack4

class OperatorAdapter(private val operatorList: ArrayList<OperatorModel>, val listner: CallBack4) : RecyclerView.Adapter<OperatorAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = OperatorListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(operatorList[position], position)
    }

    override fun getItemCount(): Int {
        return operatorList.size
    }


    inner class MyViewHolder(val binding: OperatorListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OperatorModel, position: Int) {
            binding.model=item
            ///*if (position!=items.size-1) {

            //}*/
            item.title.let {
              //  binding.tvState.text = it
            }
            item.image?.let { image ->
                Glide.with(binding.imgOperatorLogo.context)
                    .load(image)
                    .into(binding.imgOperatorLogo)
                //binding.imgOperatorLogo.setImageResource(image)
                //binding.imgOperatorLogo.setBackgroundResource(item.image)
            }
            binding.clHeader.setOnClickListener{
                item.title.let {
                    listner.getValue4(it,item.image.toString(),"","")
                }

            }
            binding.executePendingBindings()
        }
    }


}