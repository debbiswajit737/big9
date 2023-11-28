package com.epaymark.big9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.data.model.BankModeListModel
import com.epaymark.big9.databinding.BankListLayoutBinding
import com.epaymark.big9.utils.`interface`.CallBack


class BankModeListAdapter(private val items: List<BankModeListModel>, val callback: CallBack) : RecyclerView.Adapter<BankModeListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        //bank_list_layout
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: BankListLayoutBinding = BankListLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: BankListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BankModeListModel, position: Int) {
            ///*if (position!=items.size-1) {
               // binding.imageView7.setBackgroundResource(item?.bankLogo)
            //}*/
            item.bankName?.let {bankName->
                binding.tvBankName.text = bankName
                binding.llContainer.setOnClickListener{
                    callback.getValue(bankName)
                }
            }
            item.bankAc?.let {bankAc->
                binding.tvAcc.text = bankAc


            }

            item.ifsc?.let {ifsc->
                binding.tvIfsc.text = ifsc

            }

            item.bankLogo?.let {bankLogo->
                binding.imageView7.setImageResource(bankLogo)
            }


        }
    }


}