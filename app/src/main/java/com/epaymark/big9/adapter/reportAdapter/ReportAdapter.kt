package com.epaymark.big9.adapter.reportAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.databinding.ReportLayoutItemBinding
import com.epaymark.big9.data.model.ReportModel
import com.epaymark.big9.data.model.ReportPropertyModel

import com.epaymark.big9.utils.`interface`.CallBack
import com.google.gson.Gson


class ReportAdapter(
    val reportPropertyModel: ReportPropertyModel,
    private val items: List<ReportModel>,

    val callBack: CallBack
) : RecyclerView.Adapter<ReportAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /*val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_items, parent, false)
        return MyViewHolder(view)*/
       /* val binding = BannerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)*/


        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ReportLayoutItemBinding =
            ReportLayoutItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)


    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }




    inner class MyViewHolder(val binding: ReportLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReportModel, position: Int) {
            binding.apply {

                model=item
                cardView.setOnClickListener{
                    if (item.isClickAble){
                        val gson = Gson()
                        val json = gson.toJson(item)
                        callBack.getValue(json)
                    }

                }
                propertyModel=reportPropertyModel
                executePendingBindings()
            }

        }
    }


}