package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.R
import com.epaymark.big9.adapter.ReportDetailsAdapter
import com.epaymark.big9.adapter.reportAdapter.CommissionReportAdapter
import com.epaymark.big9.adapter.reportAdapter.PagingReportAdapter

import com.epaymark.big9.adapter.reportAdapter.ReportAdapter
import com.epaymark.big9.data.model.ReportModel
import com.epaymark.big9.data.model.ReportPropertyModel
import com.epaymark.big9.data.model.Reportdetails
import com.epaymark.big9.data.model.allReport.CommissionReportData
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.data.viewMovel.TableViewModel
import com.epaymark.big9.databinding.FragmentReportBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.activity.RegActivity

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.Constants.newReportList
import com.epaymark.big9.utils.helpers.Constants.reportAdapter
import com.epaymark.big9.utils.helpers.Constants.reportDetailsAdapter
import com.epaymark.big9.utils.helpers.Constants.reportDetailsPropertyList
import com.epaymark.big9.utils.helpers.Constants.reportList
import com.epaymark.big9.utils.helpers.Constants.reportList2
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.table.DataEntity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportFragment : BaseFragment()  {
    lateinit var binding: FragmentReportBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var lastClickTime1: Long = 0
    private var lastClickTime2: Long = 0
    private var lastClickTime3: Long = 0
    var isAsintask=true
    private val myViewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var startDate=""
    var endDate=""
    var startIndex = 21
    var endIndex = 30
    var isTopAsink=true
    private lateinit var recyclerView: RecyclerView
    var isScrollingLoaderShowing=false
    var isConfirmCall=true
    //private lateinit var tableViewModel: TableViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)




       // tableViewModel = ViewModelProvider(this)[TableViewModel::class.java]
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

    fun clearAllData(){
        //reportList?.clear()
        reportAdapter?.let {
           // binding.bottomLoader.visibility=View.GONE
            //reportList.clear()
            //newReportList.clear()
            it.items=ArrayList()
            it.notifyDataSetChanged()
        }
    }
    private fun onViewClick() {

        binding.apply {
          imgBack.setOnClickListener{
              reportList?.clear()
              reportAdapter?.let {
                 // binding.bottomLoader.visibility=View.GONE
                  reportList.clear()
                  newReportList.clear()
                  it.items=ArrayList()
                  it.notifyDataSetChanged()
              }
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
                /*if(isConfirmCall){
                    isConfirmCall=false*/
                tvConfirm.setBottonLoader(false,llLoader)

                    startIndex = 0
                    endIndex = 20
                    binding.loaderBottom.visibility = View.GONE
                    //binding.btnHasdata.visibility = View.GONE
                    reportAdapter?.let {
                        reportList.clear()
                        newReportList.clear()
                        it.items=ArrayList()
                        it.notifyDataSetChanged()
                    }
                    getAllData()
                //}

            }


        }
    }

    fun initView() {

        reportDetailsPropertyList = ArrayList()
        reportDetailsAdapter?.items= ArrayList()
        reportDetailsAdapter?.notifyDataSetChanged()

        startIndex = 0
        endIndex = 20
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

        }
        viewModel?.apply {
            startDate.value ="".currentdate()
            enddate.value="".currentdate()
        }

        initRecycleView()
        Handler(Looper.getMainLooper()).postDelayed({
            reportAdapter?.let {
                reportList.clear()
                newReportList.clear()
                it.items=ArrayList()
                it.notifyDataSetChanged()
            }
            getAllData()
        },100)

        backPressed()
    }

    private fun initRecycleView() {
        binding.recycleViewReport.apply {
            recyclerView = this
            reportAdapter = ReportAdapter(ReportPropertyModel(""),ArrayList(),  object : CallBack {
                override fun getValue(s: String) {
                    viewModel.reportTypeIDRecept.value=s
                    /*val bundle = Bundle()
                    bundle.putString("jsonData", s)*/
                    findNavController().navigate(
                        R.id.action_reportFragment_to_reportDetailsFragment
                    )
                }

            })
            adapter=reportAdapter

        }
    }

    private fun getAllData() {

        reportList.clear()
        reportAdapter?.let {
            reportList.clear()
            newReportList.clear()
            it.items=ArrayList()
            it.notifyDataSetChanged()
        }
        viewModel?.reportType?.value?.let { type ->
            isConfirmCall=true
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
                    loader?.show()

                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                    //reportList.add(ReportModel("001","778.00","10-10-2023","Payment send",0, desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.send_logo))
                    //reportList.add(ReportModel("002","778.00","10-10-2023","Payment received",1 ,desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.receive_logo))
                    if(!it.data?.data.isNullOrEmpty()){
                        it.data?.data?.let {responseData->
                            for (index in responseData.indices){
                                val items=responseData[index]
                                items.apply {
                                    var desc="Receiver Name:$receiverName\nReferance id - $receiverID\nSender Current Balance : $curBalSender\nSender ID : $senderID\nSender Mobile No.:$senderMobileNo"+
                                            "\nReceiver ID: $receiverID"+
                                            "\nReceiver Mobile No.: $receiverMobileNo"

                                    reportList.add(ReportModel(PaymentBYId,LastTransactionAmount,LastTransactionTime,AmountMode,0,desc,imageInt = R.drawable.send_logo))


                                }

                            }
                            showrecycleView(1)
                        }


                    }

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }


        myViewModel?.ranscationReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
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
                                            var desc="Operator :$Operator \nReferance id - $referenceID\nCustomer Mobile No.:$CustNo"
                                            reportList.add(ReportModel(TransactionID,Amount,tDate,status,statusCode,desc,imageInt,isClickAble=true, IDP = ID))
                                        }

                                    }
                                    showrecycleView(9)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.dmtReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
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

                                            var desc="$recName \nA/c No.:$recAcno\nCustomer Mobile No.:$custMobno\nUTR: $utr\nReceipt Id: $receiptid\nType: $type"

                                            reportList.add(ReportModel(tranId,tranAmt,transDt,status,statusCode,desc,imageInt,image1 = 2,isClickAble=true, IDP = receiptid))

                                        }

                                    }
                                    showrecycleView(8)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.loadRequestReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
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
                                            var donedate=""
                                            donedate="\nApproved on: $isdonedate"
                                            if (isdonedate.isNullOrEmpty()){
                                                donedate=""
                                            }
                                            var image=if (isdone=="rejected"){
                                                R.drawable.close_icon
                                            }
                                            else{
                                                R.drawable.rounded_i
                                            }
                                            var desc="Bankname $bankname\n$donedate Completed: $isdone"
                                            reportList.add(ReportModel(purchaseid,Amount,insdate,"Credit/Sales Supports",2,desc,imageInt = image))
                                        }

                                    }
                                    showrecycleView(7)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.walletLedgerReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
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
                               /* val size=if (it.data?.data?.size?:0 >=60){
                                    60
                                }
                                else{
                                    it.data?.data?.size?.minus(1)?:0
                                }*/

                               // Log.d("TAG_size", "observer: "+it.data?.data?.size)
                                it.data?.data?.let {responseData->
                                    for (index in responseData.indices){
                                    //for (index in 0 until minOf(responseData.size, size)) {
                                        val items=responseData[index]
                                        items.apply {

                                            reportList.add(ReportModel(refillid,amount,insdate,type,3,desc = "Status: $status",image1 = 2,imageInt=R.drawable.rupee_rounded,price2 = "Closing ₹$curramt",proce1TextColor = 2,isMiniStatement = false))
                                        }

                                    }
                                    showrecycleView(reportList.size)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.aepsReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
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
                                            val desc="$responseDescription " +

                                                    "\nBank Reference Number : $BankReferenceNumber"+
                                                    "\nTransaction Status  : $tranStatus"+
                                                    "\nAadhar No  : $aadharno"+
                                                    "\nBank Reference Number  : $BankReferenceNumber"+
                                                    "\nAvailable Balance  : $avbalance"
                                            reportList.add(ReportModel(tranId,tranAmt,transDt,desc,imageInt = R.drawable.close_icon,isMiniStatement = true,miniStatementValue = "${type?.replace("_"," ")}",isClickAble = true, IDP = tranId))
                                        }

                                    }
                                    showrecycleView(5)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.microatmReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {
                                            val desc="$responseDescription   " +
                                                    "\nBank Reference Number : $BankReferenceNumber" +
                                                    "\ntype : $type" +
                                                    "\nTransaction Status: $tranStatus"+
                                                    "\nPancard: $maskedPan"
                                            reportList.add(ReportModel(tranId,tranAmt,transDt,desc,imageInt = R.drawable.rounded_i, IDP = tranId, isClickAble = true))
                                        }

                                    }
                                    showrecycleView(4)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.commissionReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let { responseData ->
                                    for (items in responseData){

                                        items.apply {
                                            var comm_data:String=""

                                            comm_data=if (type=="rs"){
                                                "₹$comm"
                                            }
                                            else if (type=="%"){
                                                "$comm%"
                                            }
                                            else{
                                                comm.toString()
                                            }
                                            reportList.add(ReportModel(comm_data, reporyStatus = opname,imageInt = R.drawable.rounded_i))
                                        }

                                    }
                                    showrecycleView(12)
                                    /*CoroutineScope(Main).launch {

                                    for (index in responseData.indices) {

                                               // paging++

                                                 if (responseData.isNotEmpty()) {
                                                     responseData[index].apply {
                                                         val desc = "$opname   "
                                                         if (index<=20) {
                                                         reportList.add(
                                                             ReportModel(
                                                                 "",
                                                                 desc = desc,
                                                                 price = comm,
                                                                 imageInt = R.drawable.rounded_i
                                                             )
                                                         )
                                                     }
                                                         *//*Log.d("TAG_table", "observer: "+tableViewModel.insertData(DataEntity(responseId="",
                                                             desc = desc,
                                                             price = comm,
                                                             imageInt = R.drawable.rounded_i)))*//*
                                                             *//*reportList2.add(
                                                                 ReportModel(
                                                                     "",
                                                                     desc = desc,
                                                                     price = comm,
                                                                     imageInt = R.drawable.rounded_i
                                                                 )
                                                             )*//*

                                                     }
                                                    // showPagingRecycleView()
                                                     //showrecycleView()
                                                 }

                                            }

                                            *//*for (index in responseData.indices) {
                                               // paging++
                                             if (index<=20) {
                                                 if (responseData.isNotEmpty()) {
                                                     responseData[index].apply {
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
                                                     showrecycleView()
                                                 }
                                             }
                                            }*//*
                                        *//*if (commissionReportList.size>10){
                                            for(index in commissionReportList.indices){
                                                i
                                            }
                                            reportList
                                        }*//*



                                    }*/
                                }
                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
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
                     loader?.show()

                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    //Log.d("TAG_complain", "observeraaa: "+it.data?.Description)
                    if(!it.data?.data.isNullOrEmpty()){
                        it.data?.data?.let {responseData->
                            for (items in responseData){

                                items.apply {
                                /*
                                {
                                      "ddlComplaintCategory": "1",
                                      "ddlComplaintType": "1",
                                      "ticketID": "28",
                                      "ticketDate": "2023-10-10 11:21:44",
                                      "txtTransactionId": "300000029",
                                      "ticketFlag": "0",
                                      "status": "0",
                                      "complaintCategoryName": "BDNR",
                                      "complaintTypeName": "Recharges and Bills"
                                    }
                                 */

                                    var desc="Complaint Type $complaintTypeName " +
                                            "\nTransaction Id :$txtTransactionId"+
                                            "\nTicket ID :$ticketID"+
                                            "\nComplaint Category Name :$complaintCategoryName"
                                    reportList.add(ReportModel(txtTransactionId,"","Complaint Date $ticketDate",desc=desc))
                                }

                            }

                        }


                    }
                    showrecycleView(33)
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }



        myViewModel?.walletSettleReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                          loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {


                                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                                            var desc="$transDt \nUTR :$utr"
                                            reportList.add(ReportModel(tranId,tranAmt,"",reporyStatus=desc))
                                        }

                                    }
                                    showrecycleView(10)
                                }


                            }


                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.bank_settle_reportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {


                                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                                            var desc="$transDt \nUTR :$utr"
                                            reportList.add(ReportModel(tranId,tranAmt,"",desc=desc))
                                        }

                                    }
                                    showrecycleView(11)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }


        myViewModel?.cashout_ledger_reportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                           loader?.show()

                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
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
                            )*/

                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let {responseData->
                                    for (items in responseData){

                                        items.apply {

                                           reportList.add(ReportModel(refillid,amount,insdate,"",3,desc = "Type: $type\n Status: $status",image1 = 2,imageInt=R.drawable.rupee_rounded,price2 = "Closing ₹$curramt",proce1TextColor = 2,isMiniStatement = false))
                                        }

                                    }
                                    showrecycleView(2)
                                }


                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            binding.tvConfirm.setBottonLoader(true,binding.llLoader)
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
        /*lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                CoroutineScope(Dispatchers.Main).launch {
                    launch(Dispatchers.Main) {
                        // Perform UI-related operation here, e.g., update UI elements
                        binding.nsvTop.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                            // Check if the scroll position has changed
                            if (scrollY != oldScrollY) {
                                // Check if the NestedScrollView has reached the bottom
                                val maxScrollRange =
                                    binding.nsvTop.getChildAt(0).height - binding.nsvTop.height
                                val isAtBottom = scrollY >= maxScrollRange

                                if (isAtBottom) {

                                    // NestedScrollView is at the bottom, perform your actions here
                                    *//*if (!isDataLoadingFromLocal) {
                               // if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 10000) ){

                                    CoroutineScope(Dispatchers.Main).launch{
                                        binding.loaderBottom.visibility=View.VISIBLE
                                    }
                                CoroutineScope(Dispatchers.IO).launch {
                                    getAllData3()
                                }
                                    //isDataLoadingFromLocal=true
                                //}
                            }*//*
                                    CoroutineScope(Dispatchers.IO).launch {
                                        //getAllData3()
                                        if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                                            if (!(endIndex >= (Constants.reportList.size - 1))) {
                                                if (isAsintask) {
                                                    Log.d("TAG_s2", "observer:334 ")
                                                    //commissionReportAdapter?.setLoading(true)
                                                    CoroutineScope(Dispatchers.Main).launch {
                                                        binding.loaderBottom.visibility =
                                                            View.VISIBLE
                                                    }
                                                    lifecycleScope.launch {
                                                        withContext(Dispatchers.IO) {
                                                            CoroutineScope(Dispatchers.IO).launch {
                                                                val loadMoreDataTask =
                                                                    MyAsyncTask2()
                                                                loadMoreDataTask.execute()
                                                                isAsintask = false
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                    Log.d("TAG_s2", "observer:111 ")

                                } else {
                                    // NestedScrollView is not at the bottom
                                    Log.d("TAG_s2", "observer:222 ")

                                }
                            }
                        }
                    }

                    // Other code in the main coroutine

                }
            }
        }*/

        /*binding.nsvTop.setOnScrollChangeListener { _, _, _, _, _ ->
            val maxScrollRange = binding.nsvTop.getChildAt(0).height - binding.nsvTop.height
            val isAtBottom = binding.nsvTop.scrollY >= maxScrollRange

            if (isAtBottom) {
                // Get the last visible item position in the RecyclerView
                val layoutManager = (recyclerView.layoutManager as? LinearLayoutManager)
                val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPosition()

                // Check if the last visible item position is within the last two items
                if (lastVisibleItemPosition != null && lastVisibleItemPosition >= reportAdapter?.items?.size?.minus(
                        2
                    ) ?: 0) {

                        CoroutineScope(Dispatchers.Main).launch {
                            binding.loaderBottom.visibility = View.VISIBLE
                        }

                        if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                            if (!(endIndex >= (Constants.reportList.size - 1)) && isAsintask) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val loadMoreDataTask = MyAsyncTask2()
                                    loadMoreDataTask.execute()
                                    isAsintask = false
                                }
                            }
                        }

                }
            }
        }*/
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                // Get the first visible item position
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Get the last visible item position
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // You can use these positions as needed
                // For example, log them
               /* Log.d("RecyclerView", "First visible position: $firstVisibleItemPosition")
                Log.d("RecyclerView", "Last visible position: $lastVisibleItemPosition")
                Log.d("RecyclerView", "adapter position: ${reportAdapter?.items?.size}")
*/
                if (lastVisibleItemPosition>=(reportAdapter?.items?.size?.minus(1)?:0)) {


                    if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                        if (!(endIndex >= (Constants.reportList.size - 1)) && isAsintask) {
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.VISIBLE
                            }
                            CoroutineScope(Dispatchers.IO).launch {

                                val loadMoreDataTask = MyAsyncTask2()
                                loadMoreDataTask.execute()
                                isAsintask = false
                            }
                        }
                        else{
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.GONE
                            }
                        }
                    }
                }
                else if (firstVisibleItemPosition==0){
                    if (startIndex>0) {
                        if (!(SystemClock.elapsedRealtime() - lastClickTime2 < 3000)) {
                            if (isTopAsink) {
                               // val myAsyncTask2ScrollTop = MyAsyncTask2ScrollTop()
                              //  myAsyncTask2ScrollTop.execute()
                                isTopAsink=false
                            }
                        }
                    }
                }

                else {
                    // NestedScrollView is not at the bottom
                }


            }
        })
      /*  binding.nsvTop.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY != oldScrollY) {
                val maxScrollRange = binding.nsvTop.getChildAt(0).height - binding.nsvTop.height
                val isAtBottom = scrollY >= maxScrollRange

                Log.d("TAG_scrollY", "observer:scrollY "+scrollY)
                Log.d("TAG_scrollY", "observer:oldScrollY "+oldScrollY)
                val isAtTop = scrollY == 0
                if (isAtBottom) {


                    if (!(SystemClock.elapsedRealtime() - lastClickTime1 < 1500)) {
                        if (!(endIndex >= (Constants.reportList.size - 1)) && isAsintask) {
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.VISIBLE
                            }
                            CoroutineScope(Dispatchers.IO).launch {

                                val loadMoreDataTask = MyAsyncTask2()
                                loadMoreDataTask.execute()
                                isAsintask = false
                            }
                        }
                        else{
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.GONE
                            }
                        }
                    }
                }
                else if (isAtTop){
                    if (startIndex>0) {
                        if (!(SystemClock.elapsedRealtime() - lastClickTime2 < 3000)) {
                            if (isTopAsink) {
                                val myAsyncTask2ScrollTop = MyAsyncTask2ScrollTop()
                                myAsyncTask2ScrollTop.execute()
                                isTopAsink=false
                            }
                        }
                    }
                }

                else {
                    // NestedScrollView is not at the bottom
                }
            }
        }*/
    }

    fun showrecycleView(a:Int) {
       // Log.d("TAG_data", "showrecycleView: "+a)
        clearAllData()
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            reportAdapter?.apply {
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
                                    imageInt = R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )*/
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




                viewModel?.reportType?.value?.let { type ->

                    val setReportPropertyModel = when (type) {

                        getString(R.string.payment) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.transactions) -> {
                            ReportPropertyModel("Transaction id", "")
                        }

                        getString(R.string.dmt) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.load_Requests) -> {
                            ReportPropertyModel("Purchased Id")
                        }

                        getString(R.string.wallet_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.cashout_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.aeps) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.micro_atm) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.commissions) -> {
                            ReportPropertyModel("Commissions")
                        }

                        getString(R.string.bank_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.wallet_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.complaints) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {
                            ReportPropertyModel("Transaction id")
                        }
                    }
                    //Log.d("TAG_complain", "observeraaa:c "+reportList.size)
                    if (reportList.size > 0) {
                        binding.tvNoDataFound.visibility = View.GONE
                    } else {
                        binding.tvNoDataFound.visibility = View.VISIBLE

                    }
                    //binding.nsv.isVisible=!binding.tvNoDataFound.isVisible
                    reportPropertyModel=setReportPropertyModel
                    //items=reportList
                    lifecycleScope.launch {
                     //   binding.btnHasdata.visibility=View.GONE
                        reportList?.let {
                            if(it.size>20) {
                                getAllData2()
                            }
                            else{
                                //binding.btnHasdata.visibility=View.GONE
                                reportAdapter?.items = reportList
                                reportAdapter?.notifyDataSetChanged()
                            }
                        }
                    }
                  /*  binding.btnHasdata.setOnClickListener {
                      //  binding.btnHasdata.visibility=View.GONE
                        getAllData2()
                    }*/
                    //loadAllData()
                    /*handler.postDelayed({
                        reportAdapter.items=reportList2
                        reportAdapter.notifyDataSetChanged()
                    }, 2000)*/



                }

            }

            /*lifecycleScope.launchWhenStarted {
                tableViewModel.data.collectLatest { pagingData ->
                    reportAdapter.submitData(pagingData)
                }
            }*/
           /* lifecycleScope.launchWhenStarted {
                tableViewModel.data.observe(viewLifecycleOwner) { pagingData ->
                    reportAdapter.submitData(lifecycle, pagingData)
                }
            }
            tableViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.bottomLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            }*/
        }
    }
    private fun getAllData2() {
        loader?.show()
        if (!(endIndex >= (reportList.size - 1))) {
            for (index in reportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = reportList[index]
                    items.apply {
                        newReportList.add(this)
                    }

                }
            }

            reportAdapter?.items = newReportList
            reportAdapter?.notifyDataSetChanged()
           // binding.btnHasdata.visibility=View.VISIBLE
        } /*else {
            binding.btnHasdata.visibility = View.GONE
        }*/

        //delay(2000)
        loader?.dismiss()
        startIndex += 10
        endIndex += 10
        loader?.dismiss()
        //showrecycleView()
    }

    /*fun showPagingRecycleView() {
        val startIndex = 1
        val endIndex = 20
        val dataInRange = tableViewModel.getDataInRange(startIndex, endIndex)
        dataInRange?.forEach(){
            Log.d("TAG_id", "showPagingRecycleView: "+it?.id)
        }
       *//* val handler = Handler(Looper.getMainLooper())
        handler.post {
            binding.recycleViewReport.apply {
                //reportList.clear()

                viewModel?.reportType?.value?.let { type ->
                    when (type) {

                        getString(R.string.payment) -> {
                            // reportList.add(ReportModel("001","778.00","10-10-2023","Payment send",0, desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.send_logo))
                            //  reportList.add(ReportModel("002","778.00","10-10-2023","Payment received",1 ,desc = "AEPS-MINI_STATEMENT -9163265863\nReferance id - 30000018",imageInt = R.drawable.receive_logo))


                        }


                        getString(R.string.transactions) -> {
                            *//**//* reportList.add(
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
                    )*//**//*

                        }

                        getString(R.string.dmt) -> {
                            *//**//* reportList.add(
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
                    )*//**//*

                        }

                        getString(R.string.load_Requests) -> {
                            *//**//* reportList.add(
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
                    )*//**//*
                        }

                        getString(R.string.wallet_ledger) -> {
                            *//**//* reportList.add(
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
                    )*//**//*

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
                                    imageInt = R.drawable.rupee_rounded,
                                    price2 = "Closing ₹1021.00",
                                    proce1TextColor = 2,
                                    isMiniStatement = false
                                )
                            )
                        }

                        getString(R.string.aeps) -> {
                            *//**//*reportList.add(
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
                    )*//**//*
                        }

                        getString(R.string.micro_atm) -> {
                            // ReportPropertyModel("Transaction id")
                            //isClickAble = true
                        }

                        getString(R.string.commissions) -> {
                            //  ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.bank_settle) -> {
                            *//**//*reportList.add(
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
                    )*//**//*
                        }

                        getString(R.string.wallet_settle) -> {
                            *//**//*reportList.add(
                        ReportModel(
                            "001",
                            "10.00",
                            "10-10-2023",
                            "Failed",
                            0,
                            desc = "Type: Settle to wallet\nstatus - processed\ndetails-wallet",

                            image1 = 3
                        )
                    )*//**//*
                        }

                        getString(R.string.complaints) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {}
                    }
                }




                viewModel?.reportType?.value?.let { type ->

                    val reportPropertyModel = when (type) {

                        getString(R.string.payment) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.transactions) -> {
                            ReportPropertyModel("Transaction id", "")
                        }

                        getString(R.string.dmt) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.load_Requests) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.wallet_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.cashout_ledger) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.aeps) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.micro_atm) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.commissions) -> {
                            ReportPropertyModel("Commissions")
                        }

                        getString(R.string.bank_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.wallet_settle) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        getString(R.string.complaints) -> {
                            ReportPropertyModel("Transaction id")
                        }

                        else -> {
                            ReportPropertyModel("Transaction id")
                        }
                    }
                    recyclerView=this
                    if (reportList.size > 0) {
                        binding.tvNoDataFound.visibility = View.GONE
                    } else {
                        binding.tvNoDataFound.visibility = View.VISIBLE

                    }
                    // binding.nsv.isVisible=!binding.tvNoDataFound.isVisible
                    pagingreportAdapter = PagingReportAdapter(reportPropertyModel,  object : CallBack {
                        override fun getValue(s: String) {
                            val bundle = Bundle()
                            bundle.putString("jsonData", s)
                            findNavController().navigate(
                                R.id.action_reportFragment_to_reportDetailsFragment,
                                bundle
                            )
                        }

                    })
                    adapter=reportAdapter
                    //loadAllData()
                    *//**//*handler.postDelayed({
                        reportAdapter.items=reportList2
                        reportAdapter.notifyDataSetChanged()
                    }, 2000)*//**//*



                }

            }

            *//**//*lifecycleScope.launchWhenStarted {
                tableViewModel.data.collectLatest { pagingData ->
                    reportAdapter.submitData(pagingData)
                }
            }*//**//*
            *//**//*lifecycleScope.launchWhenStarted {
                tableViewModel.data.observe(viewLifecycleOwner) { pagingData ->
                    pagingreportAdapter.submitData(lifecycle, pagingData)
                }
            }
            tableViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.bottomLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            }*//**//*

            tableViewModel.data.observe(viewLifecycleOwner) { pagingData ->

                Log.d("TAGpagingData", "showPagingRecycleView: "+pagingData)
                pagingreportAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }

            // Observe the loading state
            tableViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                // Update UI based on the loading state
                binding.bottomLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }*//*

        // Example: Get data in a specific range

    }*/

    /*override fun onPause() {
        super.onPause()
        reportAdapter?.let {
            it.items=ArrayList()
            it.notifyDataSetChanged()
        }
    }*/
    fun backPressed(){
        activity?.let {
            it.onBackPressedDispatcher
            .addCallback(it, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (isEnabled) {
                        isEnabled = false
                        reportAdapter?.let {
                          //  binding.bottomLoader.visibility=View.GONE
                            reportList.clear()
                            newReportList.clear()
                            it.items=ArrayList()
                            it.notifyDataSetChanged()
                        }
                        it.onBackPressedDispatcher.onBackPressed()

                    }
                }
            }
            )
        }

    }


    inner class MyAsyncTask2 : AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg params: Void?) {
            // Background work (in a background thread)

            /*for (index in reportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = reportList[index]
                    items.apply {
                        newReportList.add(this)
                    }

                }
            }

            reportAdapter?.items = newReportList*/


            /*if (!(endIndex >= (reportList.size - 1))) {
               // Log.d("TAG_s2", "observer:444 ")
                for (index in startIndex until minOf(endIndex, reportList.size)) {
                   // Log.d("TAG_s2", "observer:555 ")
                    if (index >= startIndex && index <= endIndex) {
                      //  Log.d("TAG_s2", "observer:666 ")
                        val items = Constants.reportList[index]
                        items.apply {
                            //reportList2.add(items)
                            newReportList?.add(items)
                        }
                    }
                }
            }*/
            val batchSize = 10
            startIndex= reportAdapter?.itemCount?:0
            endIndex =startIndex+10
            isScrollingLoaderShowing=false
            // Ensure that the indices are within bounds
            if (startIndex < reportList.size && startIndex < endIndex) {
                val newData = reportList.subList(startIndex, minOf(endIndex, reportList.size))
                /*if (newReportList.size>39) {
                    isScrollingLoaderShowing=true
                    newReportList.clear()
                }*/
                newReportList.addAll(newData)

                // Update indices for the next batch
                startIndex = endIndex
                endIndex = minOf(endIndex + batchSize, reportList.size)
            }


        }

        override fun onPostExecute(result: Unit?) {
            // UI-related operations (in the main thread)
            /*activity?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!(endIndex >= (Constants.commissionReportList.size - 1))) {
                        reportAdapter?.notifyDataSetChanged()
                    }

                startIndex += 10
                endIndex += 10
                loader?.dismiss()
            }


        }*/

            activity?.let {
                 it.runOnUiThread(){
                CoroutineScope(Dispatchers.IO).launch {
                    CoroutineScope(Dispatchers.Main).launch {
                      //  if (!(endIndex >= (reportList.size - 1))) {

                           /* withContext(Dispatchers.Default) {
                                reportAdapter?.items=reportList
                            }*/
                            withContext(Dispatchers.Main) {
                                // Update the adapter on the main thread
                                if (isScrollingLoaderShowing){
                                    loader?.show()
                                }
                                delay(500)
                                reportAdapter?.notifyDataSetChanged()
                                if (isScrollingLoaderShowing){
                                    binding.nsvTop.scrollTo(0, 0)
                                }
                                isScrollingLoaderShowing=false
                                /*val itemCount = recyclerView.adapter?.itemCount ?: 0

                                if (itemCount >= 1) {

                                    binding.nsvTop.scrollTo(0, 0)
                                }*/
                            }

                           // reportAdapter?.notifyDataSetChanged()
                      //  }
                        loader?.dismiss()



                        CoroutineScope(Dispatchers.IO).launch {
                           /* startIndex = endIndex + 1
                            endIndex += 10*/




                            isAsintask = true
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.loaderBottom.visibility = View.GONE
                        }
                    }
                    }
                }
            }

    }

}

    inner class MyAsyncTask2ScrollTop : AsyncTask<Void, Void, Unit>() {

        override fun doInBackground(vararg params: Void?) {
            // Background work (in a background thread)

            /*for (index in reportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = reportList[index]
                    items.apply {
                        newReportList.add(this)
                    }

                }
            }

            reportAdapter?.items = newReportList*/


            /*if (!(endIndex >= (reportList.size - 1))) {
               // Log.d("TAG_s2", "observer:444 ")
                for (index in startIndex until minOf(endIndex, reportList.size)) {
                   // Log.d("TAG_s2", "observer:555 ")
                    if (index >= startIndex && index <= endIndex) {
                      //  Log.d("TAG_s2", "observer:666 ")
                        val items = Constants.reportList[index]
                        items.apply {
                            //reportList2.add(items)
                            newReportList?.add(items)
                        }
                    }
                }
            }*/
            val batchSize = 10

            isScrollingLoaderShowing=false
            if (startIndex >= batchSize) {
                // Move back to the previous batch
                startIndex -= batchSize
                endIndex = minOf(startIndex + batchSize, reportList.size)
                if (newReportList.size>39) {
                    newReportList.clear()
                }
                var arrTemp=newReportList
                newReportList.clear()


                val newData = reportList.subList(startIndex, endIndex)
                newReportList.addAll(newData)
                if (newReportList.size<=39) {
                    newReportList.addAll(arrTemp)
                }
                // Update UI or perform other actions as needed
            }


        }

        override fun onPostExecute(result: Unit?) {
            // UI-related operations (in the main thread)
            /*activity?.let {
                CoroutineScope(Dispatchers.Main).launch {
                    if (!(endIndex >= (Constants.commissionReportList.size - 1))) {
                        reportAdapter?.notifyDataSetChanged()
                    }

                startIndex += 10
                endIndex += 10
                loader?.dismiss()
            }


        }*/

            activity?.let {
                it.runOnUiThread(){
                    CoroutineScope(Dispatchers.IO).launch {
                        CoroutineScope(Dispatchers.Main).launch {
                            //  if (!(endIndex >= (reportList.size - 1))) {

                            /* withContext(Dispatchers.Default) {
                                 reportAdapter?.items=reportList
                             }*/
                            withContext(Dispatchers.Main) {
                                // Update the adapter on the main thread
                                //if (isScrollingLoaderShowing){
                                    loader?.show()
                                //}

                                reportAdapter?.notifyDataSetChanged()
                                //if (isScrollingLoaderShowing){

                                //}
                                isScrollingLoaderShowing=false
                                /*val itemCount = recyclerView.adapter?.itemCount ?: 0

                                if (itemCount >= 1) {

                                    binding.nsvTop.scrollTo(0, 0)
                                }*/
                            }

                            // reportAdapter?.notifyDataSetChanged()
                            //  }
                            delay(1000)
                            loader?.dismiss()
                            binding.nsvTop.scrollTo(0, 5)
                            isTopAsink=true


                            CoroutineScope(Dispatchers.IO).launch {
                                /* startIndex = endIndex + 1
                                 endIndex += 10*/




                                isAsintask = true
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.loaderBottom.visibility = View.GONE
                            }
                        }
                    }
                }
            }

        }

    }

}

