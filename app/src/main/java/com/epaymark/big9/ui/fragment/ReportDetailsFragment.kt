package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.helpers.Constants.reportDetailsAdapter
import com.epaymark.big9.utils.helpers.Constants.reportDetailsPropertyList
import com.google.gson.Gson
import java.lang.reflect.Field
import java.util.Locale

class ReportDetailsFragment : BaseFragment() {
    private var loader: Dialog? = null
    lateinit var binding: FragmentReportDetailsBinding
    private val viewModel: MyViewModel by activityViewModels()

    var jsonData:String=""
    private var lastClickTime1: Long = 0

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
            imgBtnShare.setOnClickListener{
                shareImage()
            }
          imgBack.back()

          }
        }


    fun initView() {
        reportDetailsPropertyList?.clear()
        reportDetailsAdapter?.items=ArrayList()
        reportDetailsAdapter?.notifyDataSetChanged()
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
        binding.recycleViewReportdetails.apply {
            reportDetailsAdapter= ReportDetailsAdapter(ArrayList())

            //reportDetailsPropertyList.add(Reportdetails("Transaction id","001"))
            adapter=reportDetailsAdapter
        }
            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            if (isLogin){
                loginResponse?.let {loginData->
                    viewModel?.apply {
                        reportDetailsPropertyList?.clear()
                        reportDetailsAdapter?.items=ArrayList()
                        reportDetailsAdapter?.notifyDataSetChanged()
                        val  data = mapOf(
                            "userid" to loginData.userid,
                            "idp" to viewModel.reportTypeIDRecept.value
                        )

                        val gson= Gson()
                        var jsonString = gson.toJson(data)
                        if (viewModel.reportType.value==getString(R.string.transactions)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                transcationReportReceipt(it, jsonString.encrypt())
                            }
                        }

                        if (viewModel.reportType.value==getString(R.string.dmt)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                dmtReportReceipt(it, jsonString.encrypt())
                            }
                        }

                        if (viewModel.reportType.value==getString(R.string.aeps)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                aepsReportReceiptReceipt(it, jsonString.encrypt())
                            }
                        }
                        if (viewModel.reportType.value==getString(R.string.micro_atm)) {
                            loginData.AuthToken?.let {
                                /*if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                    return
                                }*/
                                microatmReportReceipt(it, jsonString.encrypt())
                            }
                        }





                    }

                }
            }

        /*try {
            val jsonObject = JSONObject(jsonData)
            val propertyNames = jsonObject.keys()
            reportDetailsPropertyList.clear()
            for (propertyName in propertyNames) {
                reportDetailsPropertyList.add(Reportdetails(propertyName,jsonObject.getString("$propertyName")))
            }
            *//*for (propertyName in propertyNames) {
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


            }*//*
            //Share data
            binding.imgBtnShare.setOnClickListener{
                shareImage()
                *//*val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, jsonObject.toString())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Share using"))*//*
            }
        } catch (e: Exception) {
            // Handle any potential JSON parsing exceptions here
        }*/


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
        viewModel?.transcationReportReceiptResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    if (it.data?.responseCode==200) {
                        //reportDetailsPropertyList.clear()
                        it.data.data?.let { dataList ->
                            for (transactionData in dataList) {
                                val fields: Array<Field> = transactionData.javaClass.declaredFields

                                for (field in fields) {
                                    field.isAccessible = true
                                    val key = field.name
                                    var value =""
                                    field.get(transactionData)?.let {
                                         value = field.get(transactionData) as String
                                    }

                                    reportDetailsPropertyList.add(Reportdetails(property=key.addSpaceBeforeUppercase(),reportValue=value))

                                }
                            }
                        }


                     /*   it.data.data?.let {dataList->

                            for (responseData in dataList) {
                                for ((key, value) in responseData) {

                                }
                                reportDetailsPropertyList.add(Reportdetails(property=key,reportValue=value))
                                println("Key: $key, Value: $value")
                            }

                    }*/


                            /*it.data.data?.let {
                                Toast.makeText(requireContext(), ""+it, Toast.LENGTH_SHORT).show()
                                for((key,value) in it){
                                    reportDetailsPropertyList.add(Reportdetails(property=key,reportValue=value))

                                }
                            }*/


                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null
                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel?.transcationReportReceiptResponseLiveData?.value=null
                }
            }
        }

        viewModel?.dmtReportReceiptResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    if (it.data?.responseCode==200) {
                        reportDetailsPropertyList.clear()
                        it.data.data?.let { dataList ->
                            for (transactionData in dataList) {
                                val fields: Array<Field> = transactionData.javaClass.declaredFields

                                for (field in fields) {
                                    field.isAccessible = true
                                    val key = field.name
                                    var value =""
                                    field.get(transactionData)?.let {
                                        value = field.get(transactionData) as String
                                    }
                                    reportDetailsPropertyList.add(Reportdetails(property=key.addSpaceBeforeUppercase(),reportValue=value))

                                       /* var  key2=if (key=="tran_amt"){
                                            "Transaction Id"
                                        }
                                        else if (key=="tran_status"){
                                            "Customer Number"
                                        }
                                        else if (key=="trans_dt"){
                                            "Status"
                                        }
                                        else if (key=="rec_name"){
                                            "Amount"
                                        }
                                        else if (key=="rec_acno"){
                                            "Balance"
                                        }
                                        else if (key=="total_tran_amt"){
                                            "Operator"
                                        }
                                        else if (key=="bank_name"){
                                            "Operator ID"
                                        }
                                        else if (key=="cust_mobno"){
                                            "Operator ID"
                                        }


                                        else{
                                            ""
                                        }



                                    if (key2!=""){
                                        reportDetailsPropertyList.add(Reportdetails(property=key,reportValue=value))
                                    }*/


                                }
                            }
                        }




                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null
                    }
                    viewModel?.dmtReportReceiptResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.dmtReportReceiptResponseLiveData?.value=null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }

        viewModel?.aepsReportReceiptReceiptResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                        reportDetailsPropertyList.clear()
                        it.data?.data?.let { dataList ->
                            for (transactionData in dataList) {
                                val fields: Array<Field> = transactionData.javaClass.declaredFields

                                for (field in fields) {
                                    field.isAccessible = true
                                    var key = field.name

                                    var value ="-"
                                    field.get(transactionData)?.let {
                                        value = field.get(transactionData) as String
                                    }
                                    reportDetailsPropertyList.add(Reportdetails(property=key.addSpaceBeforeUppercase(),reportValue=value.replace("_"," ")))
                                  /*  Log.d("TAG_key", "setObserver: "+key+" value:"+value)
                                   if (key=="transaction_id"){


                                    }
                                    else if (key=="customer_number"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Customer Number",reportValue=value))

                                    }
                                   else if (key=="amount"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Amount",reportValue=value))

                                   }
                                    else if (key== "TransactionDate"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Transaction Date",reportValue=value))
                                    }
                                     else if (key== "operator"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Operator",reportValue=value.replace("-"," ")))
                                    }


                                    else if (key=="current_balance"){
                                       reportDetailsPropertyList.add(Reportdetails(property="Current Balance",reportValue=value))

                                    }

*/

                                }
                            }
                        }





                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null

                    viewModel?.aepsReportReceiptReceiptResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.aepsReportReceiptReceiptResponseLiveData?.value=null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


        viewModel?.microatm_report_receiptResponseReceptLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                        //reportDetailsPropertyList.clear()
                        it.data?.data?.let { dataList ->
                            for (transactionData in dataList) {
                                val fields: Array<Field> = transactionData.javaClass.declaredFields

                                for (field in fields) {
                                    field.isAccessible = true
                                    var key = field.name

                                    var value =""
                                    field.get(transactionData)?.let {
                                        value = field.get(transactionData) as String
                                    }

                                    reportDetailsPropertyList.add(
                                        Reportdetails(
                                            property = key.addSpaceBeforeUppercase(),
                                            reportValue = value
                                        )
                                    )


                                    var key2=if (key=="customer_number"){
                                        "Customer Number"
                                    }


                                    else if (key=="amount"){
                                        "Amount"
                                    }
                                    else if (key=="operator"){
                                        ""
                                    }
                                    else if (key=="BankReferenceNumber"){
                                        "Bank Reference Number"
                                    }
                                    else if (key=="avbalance"){
                                        "Available Balance"
                                    }
                                    else if (key=="response_description"){
                                        "Description "
                                    }
                                    else if (key=="operator"){
                                        ""
                                    }

                                    else{
                                        ""
                                    }
                                    if (key2!="") {
                                        reportDetailsPropertyList.add(
                                            Reportdetails(
                                                property = key2,
                                                reportValue = value
                                            )
                                        )
                                    }

                                }
                            }
                        }





                        reportDetailsAdapter?.items=reportDetailsPropertyList
                        reportDetailsAdapter?.notifyDataSetChanged()
                        viewModel?.transcationReportReceiptResponseLiveData?.value=null

                    viewModel?.microatm_report_receiptResponseReceptLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.microatm_report_receiptResponseReceptLiveData?.value=null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }






    }

    fun String.addSpaceBeforeUppercase(): String {
        var temp:String=this.lowercase()
            .replace("trans","Transaction ")
            .replace("tran","Transaction ")

            .replace("dt","Date ")
            .replace("amt","Amount ")
            .replace("rec","Receiver ")
            .replace("acno","Account No.: ")
            .replace("cust","Customer ")
            .replace("mobno","Mobile No.: ")
        return temp.replace(Regex("(?<=\\p{Ll})(?=\\p{Lu})"), " ").capitalize()
    }


}