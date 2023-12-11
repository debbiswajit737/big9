package com.epaymark.big9.ui.fragment.report


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.R
import com.epaymark.big9.adapter.reportAdapter.CommissionReportAdapter
import com.epaymark.big9.adapter.reportAdapter.PagingReportAdapter

import com.epaymark.big9.data.model.ReportPropertyModel
import com.epaymark.big9.data.model.allReport.CommissionReportData

import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.data.viewMovel.TableViewModel
import com.epaymark.big9.databinding.CommissionFragmentReportBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.helpers.Constants.reportAdapter
import com.epaymark.big9.utils.helpers.Constants.reportList
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.table.DataEntity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class CommissionReportFragment : BaseFragment() {
    lateinit var binding: CommissionFragmentReportBinding
    private val viewModel: MyViewModel by activityViewModels()
    var commissionReportAdapter: CommissionReportAdapter?=null
    var commissionReportList = ArrayList<CommissionReportData>()
    private val myViewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var startDate=""
    var endDate=""
    val startIndex = 0
    val endIndex = 10
    private lateinit var recyclerView: RecyclerView

    private lateinit var pagingreportAdapter: PagingReportAdapter
    private var arryList = mutableListOf<String>() // Replace with your actual data type

    private val itemsPerPage = 10
    private var currentPage = 0
    private var isLoading = false

    private lateinit var tableViewModel: TableViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.commission_fragment_report, container, false)
        tableViewModel = ViewModelProvider(this)[TableViewModel::class.java]
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
        reportAdapter?.let {
            it.items=ArrayList()
            it.notifyDataSetChanged()
        }
        getAllData()
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


    }

    private fun getAllData() {
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



    private fun observer() {



        myViewModel?.commissionReportResponseLiveData?.observe(viewLifecycleOwner){
                    when (it) {
                        is ResponseState.Loading -> {
                            loader?.show()
                        }

                        is ResponseState.Success -> {
                            loader?.dismiss()
                            //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                            if(!it.data?.data.isNullOrEmpty()){
                                it.data?.data?.let { responseData ->

                                    lifecycleScope.launch {
                                        /*if (responseData.isNotEmpty()) {
                                            commissionReportList =responseData
                                        }*/
                                        for (index in responseData.indices) {
                                            if (responseData.isNotEmpty()) {
                                                responseData[index].apply {
                                                    tableViewModel.insertData(DataEntity(desc="${responseData[index].opname} Commission ${responseData[index].comm}${responseData[index].type}"))
                                                }
                                            }
                                        }
                                   /* for (index in responseData.indices) {

                                               // paging++

                                                 if (responseData.isNotEmpty()) {
                                                     commissionReportList=
                                                     responseData[index].apply {
                                                         val desc = "$opname   "
                                                         if (index<=20) {
                                                             commissionReportList.add(
                                                             ReportModel(
                                                                 "",
                                                                 desc = desc,
                                                                 price = comm,
                                                                 imageInt = R.drawable.rounded_i
                                                             )
                                                         )
                                                     }


                                                     }
                                                    // showPagingRecycleView()

                                                 }

                                            }*/
                                        showrecycleView()




                                    }
                                }
                            }

                        }

                        is ResponseState.Error -> {
                            loader?.dismiss()
                            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                        }
                    }
                }









    }

    fun showrecycleView() {
        lifecycleScope.launch {
            var data = tableViewModel.getDataInRange(0, 10)
            data?.forEach{
                Log.d("TAGp", "showrecycleView: "+it?.desc)
            }
        }
        /*val handler = Handler(Looper.getMainLooper())
        handler.post {
            binding.recycleViewReport.apply {
                //reportList.clear()

                commissionReportAdapter = CommissionReportAdapter(commissionReportList,  object : CallBack {
                    override fun getValue(s: String) {
                        val bundle = Bundle()
                        bundle.putString("jsonData", s)
                        findNavController().navigate(
                            R.id.action_reportFragment_to_reportDetailsFragment,
                            bundle
                        )
                    }

                })
                adapter=commissionReportAdapter






            }


        }*/
    }





}

