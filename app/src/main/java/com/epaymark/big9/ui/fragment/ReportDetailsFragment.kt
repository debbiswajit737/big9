package com.epaymark.big9.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.epaymark.big9.R

import com.epaymark.big9.adapter.ReportDetailsAdapter
import com.epaymark.big9.data.model.Reportdetails
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentReportDetailsBinding

import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.base.BaseFragment
import org.json.JSONObject

class ReportDetailsFragment : BaseFragment() {
    lateinit var binding: FragmentReportDetailsBinding
    private val viewModel: MyViewModel by activityViewModels()
    var reportDetailsPropertyList = ArrayList<Reportdetails>()
    var jsonData:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_details, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("jsonData")?.let {
            jsonData=it
        }
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {

        binding.apply {
          imgBack.back()

          }
        }


    fun initView() {
        try {


            val jsonObject = JSONObject(jsonData)



            val propertyNames = jsonObject.keys()
            reportDetailsPropertyList.clear()
            for (propertyName in propertyNames) {
                reportDetailsPropertyList.add(Reportdetails(propertyName,jsonObject.getString("$propertyName")))
            }
            /*for (propertyName in propertyNames) {
                println("Property Name: $propertyName")
                val value1 = jsonObject.getString("$propertyName")
                Log.d("TAG_value", "initView: "+value1)


                val textView = TextView(binding.root.context)
                textView.text = "$propertyName : $value1"
                textView.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                // Customize the appearance (e.g., text size, text color, etc.)
                textView.textSize = 18f
                textView.setTextColor(resources.getColor(android.R.color.black))

                // Add the dynamic TextView to the parent layout
                binding.linearLayout.addView(textView)


            }*/
            //Share data
            binding.imgBtnShare.setOnClickListener{
                shareImage()
                /*val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, jsonObject.toString())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Share using"))*/
            }
        } catch (e: Exception) {
            // Handle any potential JSON parsing exceptions here
        }
        binding.recycleViewReportdetails.apply {


            //reportDetailsPropertyList.add(Reportdetails("Transaction id","001"))
            adapter= ReportDetailsAdapter(reportDetailsPropertyList)
        }

    }
    private fun shareImage() {
        activity?.let {
            binding.apply {
                var screenshotBitmap =cardView2.takeScreenshot2()
                (activity as? DashboardActivity)?.shareImage(screenshotBitmap)
            }
        }
    }
    fun setObserver() {
        binding.apply {

        }

    }
}