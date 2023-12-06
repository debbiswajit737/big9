package com.epaymark.big9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.databinding.AccountDetailsLayoutBinding
import com.epaymark.big9.data.model.AccountDetailsModel

import com.epaymark.epay.utils.`interface`.CallBack


class AccountDetailsAdapter(private val items: ArrayList<AccountDetailsModel>, val callback: CallBack) : RecyclerView.Adapter<AccountDetailsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //report_layout_item

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: AccountDetailsLayoutBinding = AccountDetailsLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: AccountDetailsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccountDetailsModel, position: Int) {
            binding.model=item
            binding.cardContainer.setOnClickListener{
                item.acc?.let {acc->
                    if (item.approvedStatue) {
                        callback.getValue(acc)
                    }
                }

            }
            binding.executePendingBindings()
        }
    }


}