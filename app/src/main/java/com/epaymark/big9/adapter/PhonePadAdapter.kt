package com.epaymark.big9.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.databinding.KeypadLayoutBinding

import com.epaymark.epay.utils.`interface`.KeyPadOnClickListner

class PhonePadAdapter(private val items: List<Int>, val keyPadOnClickListner: KeyPadOnClickListner) : RecyclerView.Adapter<PhonePadAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: KeypadLayoutBinding = KeypadLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class MyViewHolder(val binding: KeypadLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            binding.apply {
                item.let {key->
                    constKay.setOnClickListener{
                        keyPadOnClickListner.onClick(item)
                    }

                    if (key<=9) {
                        tvKey.text = key.toString()
                        imgBack.visibility= View.GONE
                        tvKey.visibility= View.VISIBLE
                    }
                    else if (key==10){
                        tvKey.text =""
                        imgBack.visibility= View.GONE
                        tvKey.visibility= View.GONE
                    }
                    else {
                        imgBack.visibility= View.VISIBLE
                        tvKey.visibility= View.GONE
                    }
                }
            }


        }
    }


}