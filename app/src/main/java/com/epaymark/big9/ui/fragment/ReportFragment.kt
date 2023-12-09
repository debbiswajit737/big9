package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.adapter.reportAdapter.ReportAdapter
import com.epaymark.big9.data.model.ReportModel
import com.epaymark.big9.data.model.ReportPropertyModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentReportBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.activity.RegActivity

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.`interface`.CallBack
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class ReportFragment : BaseFragment() {
    lateinit var binding: FragmentReportBinding
    private val viewModel: MyViewModel by activityViewModels()
    var reportList = ArrayList<ReportModel>()
    private val myViewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var startDate=""
    var endDate=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observer()
        onViewClick()
    }

    override fun onResume() {
        super.onResume()
        //reportList?.clear()
    }
    private fun onViewClick() {

        binding.apply {
          imgBack.setOnClickListener{
              reportList?.clear()
              findNavController().popBackStack()
          }
          //imgBack.back()

          tvStartDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.startDate?.value=s
                    }

                })
            }

            tvEndDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.enddate?.value=s
                    }

                })
            }

            tvConfirm.setOnClickListener{
                getAllData()
            }


        }
    }

    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
        viewModel?.apply {
            startDate.value ="".currentdate()
            enddate.value="".currentdate()
        }
        getAllData()
    }

    private fun getAllData() {
        reportList.clear()
        viewModel?.reportType?.value?.let { type ->
            when (type) {


                getString(R.string.payment) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                    loginResponse?.let {loginData->
                        val data = mapOf(
                            "userid" to loginData.userid,
                            "startdate" to startDate,
                            "enddate" to endDate,
                        )
                        val gson= Gson()
                        var jsonString = gson.toJson(data)
                        /*val requestBody = """
                        {
                        "userid": ${loginData.userid},
                        "startdate": "07-12-2022",
                        "enddate": "07-12-2023"
                        }
                        """.trimIndent()*/
                        /*val requestBody = """${jsonString.encrypt()}"""*/


                        loginData.AuthToken?.let {
                            myViewModel?.paymentReport(it,jsonString.encrypt())
                           // loader?.show()

                        }
                    }
                }
                }


                getString(R.string.transactions) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.transcationReport(it,jsonString.encrypt())
                               // loader?.show()
                            }
                        }
                    }

                }

                getString(R.string.dmt) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.dmtReport(it,jsonString.encrypt())
                              //  loader?.show()
                            }
                        }
                    }

                }

                getString(R.string.load_Requests) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.loadRequestReport(it,jsonString.encrypt())
                           //     loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.wallet_ledger) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.walletLedgerReport(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.cashout_ledger) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.cashout_ledger_report(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.aeps) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.aepsReport(it,jsonString.encrypt())
                             //   loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.micro_atm) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.microatmReport(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.commissions) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.commissionReport(it,jsonString.encrypt())
                           //     loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.bank_settle) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.bank_settle_report(it,jsonString.encrypt())
                             //   loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.wallet_settle) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.walletSettleReport(it,jsonString.encrypt())
                            //    loader?.show()
                            }
                        }
                    }
                }

                getString(R.string.complaints) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.complaints_report(it,jsonString.encrypt())
                                    loader?.show()
                            }
                        }
                    }
                }


                /*getString(R.string.complaints) -> {
                    reportList?.clear()
                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "startdate" to startDate,
                                "enddate" to endDate,
                            )
                            val gson= Gson()
                            var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                myViewModel?.complaints_report(it,jsonString.encrypt())
                             //   loader?.show()
                            }
                        }
                    }
                }*/

                else -> {}

            }
        }
    }



    private fun observer() {

        viewModel?.startDate?.observe(viewLifecycleOwner){
            startDate=it
        }
        viewModel?.enddate?.observe(viewLifecycleOwner){
           endDate=it
        }

        myViewModel?.paymentReportResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                   // loader?.show()
                }

                is ResponseState.Success -> {
                    //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                    //reportList.add(ReportModel("001","778.00","10-10-2023","Payment send",0, desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.send_logo))
                    //reportList.add(ReportModel("002","778.00","10-10-2023","Payment received",1 ,desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.receive_logo))
                    if(!it.data?.data.isNullOrEmpty()){
                        it.data?.data?.let {responseData->
                            for (items in responseData){
                                items.apply {
                                    var desc="Referance id - $receiverID"
                                    reportList.add(ReportModel(PaymentBYId,LastTransactionAmount,LastTransactionTime,AmountMode,0,desc,imageInt = R.drawable.send_logo))
                                }

                            }
                            showrecycleView()
                        }


                    }

                }

                is ResponseState.Error -> {
                    //   loadingPopup?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


        myViewModel?.ranscationReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                           // loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            reportList.clear()
                            /*reportList.add(
                                ReportModel(
                                    "001",
                                    "778.00",
                                    "10-10-2023",
                                    "Failed",
                                    0,
                                    desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                                    imageInt = R.drawable.close_icon,
                                    isClickAble = true
                                )
                            )
                            reportList.add(
                                ReportModel(
                                    "002",
                                    "778.00",
                                    "10-10-2023",
                                    getString(R.string.success),
                                    1,
                                    desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                                    imageInt = R.drawable.right_tick
                                )
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){
                                        var status =""
                                        var statusCode =0
                                        var imageInt =0
                                        items.apply {
                                            if (Status?.lowercase()=="success") {
                                                status=getString(R.string.success)
                                                statusCode=1
                                                imageInt=R.drawable.right_tick
                                            }
                                            else {
                                                status = Status.toString()
                                                statusCode=0
                                                imageInt=R.drawable.close_icon
                                            }
                                            var desc="Operator :$Operator \nReferance id - $referenceID"
                                            reportList.add(ReportModel(TransactionID,Amount,tDate,status,statusCode,desc,imageInt))
                                        }

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.dmtReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                           // loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            /*reportList.add(
                                ReportModel(
                                    "001",
                                    "778.00",
                                    "10-10-2023",
                                    "Refunded",
                                    0,
                                    desc = "Rajiv\nA/c No.:111111111111\nSender: 5555555555",
                                    imageInt = R.drawable.imps_logo,
                                    image1 = 2,
                                    isClickAble = true
                                )
                            )
                            reportList.add(
                                ReportModel(
                                    "002",
                                    "778.00",
                                    "10-10-2023",
                                    getString(R.string.success),
                                    1,
                                    desc = "Jhuma Chowdhary\nA/c No.:000000000000\nSender :8888888888",
                                    imageInt = R.drawable.imps_logo,
                                    image1 = 2
                                )*/
                            Log.d("TAG_complain", "observeraaa: "+it.data?.Description)
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            var status =""
                                            var statusCode =0
                                            var imageInt =0
                                            if (tranStatus?.lowercase()=="success") {
                                                getString(R.string.success)
                                                statusCode=1
                                                imageInt=R.drawable.right_tick
                                            }
                                            else {
                                                status = tranStatus.toString()
                                                statusCode=0
                                                imageInt=R.drawable.close_icon
                                            }

                                            var desc="$recName \nA/c No.:$recAcno\\nCustomer :$custMobno"
                                            reportList.add(ReportModel(tranId,tranAmt,transDt,status,statusCode,desc,imageInt,image1 = 2))
                                        }

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.loadRequestReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                         //   loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            /*reportList.add(
                            ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nPayment Ref id- 5376254\nApproved on 2023-10-30",
                            imageInt = R.drawable.right_tick
                            )
                            )
                            reportList.add(
                            ReportModel(
                            "001",
                            "778.00",
                            "10-10-2023",
                            "Credit/Sales Supports",
                            2,
                            desc = "Axis Bank-Online\nSame Bank\nPayment Ref Id: ASEESSS",
                            imageInt = R.drawable.rounded_i
                            )
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            var desc="Bankname $bankname\nApproved on$isdonedate"
                                            reportList.add(ReportModel(purchaseid,Amount,insdate,"Credit/Sales Supports",2,desc,imageInt = R.drawable.rounded_i))
                                        }

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.walletLedgerReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                          //  loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            /*reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt=R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )
                            reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt=R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {

                                            reportList.add(ReportModel(refillid,amount,insdate,type,3,desc = "",image1 = 2,imageInt=R.drawable.rupee_rounded,price2 = "Closing ₹$curramt",proce1TextColor = 2,isMiniStatement = false))
                                        }

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.aepsReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                          //  loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                         /*   reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",

                                desc = "AAdhar No.:xxxx-xxxx-1458\nRRN: Balance 0\nSettltment Transaction id: 300000312",
                                imageInt = R.drawable.close_icon,
                                isMiniStatement = true,
                                miniStatementValue = "Mini Statement",
                                isClickAble = true
                            )
                        )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            val desc="$responseDescription \nNo.:$aadharno  \nBank Reference Number : $BankReferenceNumber"
                                            reportList.add(ReportModel(tranId,tranAmt,transDt,desc,imageInt = R.drawable.close_icon,isMiniStatement = true,miniStatementValue = "$type",isClickAble = true))
                                        }

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.microatmReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                          //  loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            val desc="$responseDescription   \nBank Reference Number : $BankReferenceNumber"
                                            reportList.add(ReportModel(tranId,tranAmt,transDt,desc,imageInt = R.drawable.rounded_i))
                                        }

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.commissionReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                           // loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let { responseData ->
                                    var paging=0
                                    CoroutineScope(Main).launch {
                                        if (paging!=10){

                                            //for (items in responseData) {
                                               // paging++
                                            if (responseData.isNotEmpty()) {
                                                responseData[0].apply {
                                                    val desc = "$opname   "
                                                    reportList.add(
                                                        ReportModel(
                                                            "",
                                                            desc = desc,
                                                            price = comm,
                                                            imageInt = R.drawable.rounded_i
                                                        )
                                                    )
                                                }
                                            }
                                            //}
                                            showrecycleView()
                                        }

                                    }
                                }
                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        /*myViewModel?.complaints_reportReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            Log.d("TAG_complain", "observer: "+it.data?.Description)
                            loader?.dismiss()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        *//*items.apply {
                                            val desc="$complaintTypeName   \nTransactionId : $txtTransactionId"
                                            reportList.add(ReportModel(ticketID,"",ticketDate,desc,imageInt = R.drawable.rounded_i))
                                        }*//*

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }*/

        myViewModel?.complaints_reportReportResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    // loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                    //Log.d("TAG_complain", "observeraaa: "+it.data?.Description)
                    if(!it.data?.data.isNullOrEmpty()){
                        it.data?.data?.let {responseData->
                            for (items in responseData){

                                items.apply {


                                    var desc="$complaintTypeName \nTransaction Id :$txtTransactionId"
                                    reportList.add(ReportModel(txtTransactionId,"","Booked on $ticketDate",desc=desc))
                                }

                            }
                            showrecycleView()
                        }


                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }



        myViewModel?.walletSettleReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                          //  loader?.show()
                        }

                        is ResponseState.Success -> {
                            Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.bank_settle_reportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                           // loader?.show()
                        }

                        is ResponseState.Success -> {
                            Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.cashout_ledger_reportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                           // loader?.show()
                        }

                        is ResponseState.Success -> {
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            reportList.add(
                                ReportModel(
                                    "001",
                                    "-778.00",
                                    "10-10-2023\n" +
                                            "05:49:11",
                                    "ePotlyNB Money\nForward",
                                    3,
                                    desc = "",
                                    image1 = 2,
                                    imageInt=R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {

                                           // reportList.add(ReportModel(AccountID,amount,insdate,"",3,desc = "",image1 = 2,imageInt=R.drawable.rupee_rounded,price2 = "Closing ₹$curramt",proce1TextColor = 2,isMiniStatement = false))
                                        }

                                    }
                                    showrecycleView()
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        /*myViewModel?.transcation_report_receiptResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()
                        }

                        is ResponseState.Success -> {
                            Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                        }

                        is ResponseState.Error -> {
                            //   loadingPopup?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }*/



    }

    fun showrecycleView(){
        binding.recycleViewReport.apply {
            //reportList.clear()

            viewModel?.reportType?.value?.let { type ->
                when (type) {

                    getString(R.string.payment) -> {
                       // reportList.add(ReportModel("001","778.00","10-10-2023","Payment send",0, desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.send_logo))
                      //  reportList.add(ReportModel("002","778.00","10-10-2023","Payment received",1 ,desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.receive_logo))


                    }


                    getString(R.string.transactions) -> {
                       /* reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",
                                "Failed",
                                0,
                                desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                                imageInt = R.drawable.close_icon,
                                isClickAble = true
                            )
                        )
                        reportList.add(
                            ReportModel(
                                "002",
                                "778.00",
                                "10-10-2023",
                                getString(R.string.success),
                                1,
                                desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",
                                imageInt = R.drawable.right_tick
                            )
                        )*/

                    }

                    getString(R.string.dmt) -> {
                       /* reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",
                                "Refunded",
                                0,
                                desc = "Rajiv\nA/c No.:111111111111\nSender: 5555555555",
                                imageInt = R.drawable.imps_logo,
                                image1 = 2,
                                isClickAble = true
                            )
                        )
                        reportList.add(
                            ReportModel(
                                "002",
                                "778.00",
                                "10-10-2023",
                                getString(R.string.success),
                                1,
                                desc = "Jhuma Chowdhary\nA/c No.:000000000000\nSender :8888888888",
                                imageInt = R.drawable.imps_logo,
                                image1 = 2
                            )
                        )*/

                    }

                    getString(R.string.load_Requests) -> {
                       /* reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",
                                "Credit/Sales Supports",
                                2,
                                desc = "Axis Bank-Online\nPayment Ref id- 5376254\nApproved on 2023-10-30",
                                imageInt = R.drawable.right_tick
                            )
                        )
                        reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",
                                "Credit/Sales Supports",
                                2,
                                desc = "Axis Bank-Online\nSame Bank\nPayment Ref Id: ASEESSS",
                                imageInt = R.drawable.rounded_i
                            )
                        )*/
                    }

                    getString(R.string.wallet_ledger) -> {
                       /* reportList.add(
                            ReportModel(
                                "001",
                                "-778.00",
                                "10-10-2023\n" +
                                        "05:49:11",
                                "ePotlyNB Money\nForward",
                                3,
                                desc = "",
                                image1 = 2,
                                imageInt=R.drawable.rupee_rounded,
                                price2 = "Closing ₹1021.00",
                                proce1TextColor = 2,
                                isMiniStatement = false
                            )
                        )
                        reportList.add(
                            ReportModel(
                                "001",
                                "-778.00",
                                "10-10-2023\n" +
                                        "05:49:11",
                                "ePotlyNB Money\nForward",
                                3,
                                desc = "",
                                image1 = 2,
                                imageInt=R.drawable.rupee_rounded,
                                price2 = "Closing ₹1021.00",
                                proce1TextColor = 2,
                                isMiniStatement = false
                            )
                        )*/

                    }

                    getString(R.string.cashout_ledger) -> {
                        reportList.add(
                            ReportModel(
                                "001",
                                "-778.00",
                                "10-10-2023\n" +
                                        "05:49:11",
                                "ePotlyNB Money\nForward",
                                3,
                                desc = "",
                                image1 = 2,
                                imageInt=R.drawable.rupee_rounded,
                                price2 = "Closing ₹1021.00",
                                proce1TextColor = 2,
                                isMiniStatement = false
                            )
                        )
                    }

                    getString(R.string.aeps) -> {
                        /*reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",

                                desc = "AAdhar No.:xxxx-xxxx-1458\nRRN: Balance 0\nSettltment Transaction id: 300000312",
                                imageInt = R.drawable.close_icon,
                                isMiniStatement = true,
                                miniStatementValue = "Mini Statement",
                                isClickAble = true
                            )
                        )*/
                    }

                    getString(R.string.micro_atm) -> {
                       // ReportPropertyModel("Transaction id")
                        //isClickAble = true
                    }

                    getString(R.string.commissions) -> {
                      //  ReportPropertyModel("Transaction id")
                    }

                    getString(R.string.bank_settle) -> {
                        /*reportList.add(
                            ReportModel(
                                "001",
                                "778.00",
                                "10-10-2023",
                                "Failed",
                                0,
                                desc = "Type: Settle to bank",
                                isClickAble = true,
                                image1 = 3
                            )
                        )*/
                    }

                    getString(R.string.wallet_settle) -> {
                        /*reportList.add(
                            ReportModel(
                                "001",
                                "10.00",
                                "10-10-2023",
                                "Failed",
                                0,
                                desc = "Type: Settle to wallet\nstatus - processed\ndetails-wallet",

                                image1 = 3
                            )
                        )*/
                    }

                    getString(R.string.complaints) -> {
                        ReportPropertyModel("Transaction id")
                    }

                    else -> {}
                }
            }




            viewModel?.reportType?.value?.let {type->

                val reportPropertyModel=   when(type){

                    getString(R.string.payment)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.transactions)->{
                        ReportPropertyModel("Transaction id","")
                    }
                    getString(R.string.dmt)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.load_Requests)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.wallet_ledger)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.cashout_ledger)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.aeps)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.micro_atm)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.commissions)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.bank_settle)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.wallet_settle)->{
                        ReportPropertyModel("Transaction id")
                    }
                    getString(R.string.complaints)->{
                        ReportPropertyModel("Transaction id")
                    }

                    else -> {
                        ReportPropertyModel("Transaction id")
                    }
                }
                if (reportList.size>0){
                    binding.tvNoDataFound.visibility=View.GONE
                }else{
                    binding.tvNoDataFound.visibility=View.VISIBLE

                }
                binding.nsv.isVisible=!binding.tvNoDataFound.isVisible
                adapter= ReportAdapter(reportPropertyModel,reportList,  object : CallBack {
                    override fun getValue(s: String) {
                        val bundle = Bundle()
                        bundle.putString("jsonData", s)
                        findNavController().navigate(R.id.action_reportFragment_to_reportDetailsFragment,bundle)
                    }

                })
            }

        }
    }


}