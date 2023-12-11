package com.epaymark.big9.ui.fragment.report


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommissionReportFragment : BaseFragment() {
    lateinit var binding: CommissionFragmentReportBinding
    private val viewModel: MyViewModel by activityViewModels()
    var commissionReportAdapter: CommissionReportAdapter? = null
    var commissionReportList = ArrayList<CommissionReportData>()
    var commissionReportList2 = ArrayList<CommissionReportData>()
    private val myViewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var startDate = ""
    var endDate = ""
    private var lastClickTime1: Long = 0
    var startIndex = 0
    var endIndex = 10
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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.commission_fragment_report, container, false)
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
        /* reportAdapter?.let {
             it.items=ArrayList()
             it.notifyDataSetChanged()
         }*/
        //getAllData()
    }

    private fun onViewClick() {

        binding.apply {
            imgBack.setOnClickListener {
                reportList?.clear()
                findNavController().popBackStack()
            }
            //imgBack.back()

            tvStartDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.startDate?.value = s
                    }

                })
            }

            tvEndDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.enddate?.value = s
                    }

                })
            }

            tvConfirm.setOnClickListener {
                getAllData()
            }


        }
    }

    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

        }
        viewModel?.apply {
            startDate.value = "".currentdate()
            enddate.value = "".currentdate()
        }
        binding.recycleViewReport.apply {
            recyclerView = this
            commissionReportAdapter = CommissionReportAdapter(ArrayList(), object : CallBack {
                override fun getValue(s: String) {
                    val bundle = Bundle()
                    bundle.putString("jsonData", s)
                    findNavController().navigate(
                        R.id.action_reportFragment_to_reportDetailsFragment,
                        bundle
                    )
                }

            })
            adapter = commissionReportAdapter
        }
        getAllData()
    }

    private fun getAllData() {
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        if (isLogin) {
            loginResponse?.let { loginData ->
                val data = mapOf(
                    "userid" to loginData.userid,
                    "startdate" to startDate,
                    "enddate" to endDate,
                )
                val gson = Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    myViewModel?.commissionReport(it, jsonString.encrypt())
                    //     loader?.show()
                }
            }
        }
    }


    private fun observer() {


        myViewModel?.commissionReportResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    //Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                    if (!it.data?.data.isNullOrEmpty()) {
                        it.data?.data?.let { responseData ->

                            lifecycleScope.launch {
                                /*if (responseData.isNotEmpty()) {
                                    commissionReportList =responseData
                                }*/
                                for (index in responseData.indices) {
                                    if (responseData.isNotEmpty()) {
                                        responseData[index].apply {
                                            commissionReportList.add(
                                                CommissionReportData(
                                                    responseData[index].opname,
                                                    " Commission ${responseData[index].comm}",
                                                    responseData[index].type
                                                )
                                            )
                                            // tableViewModel.insertData(DataEntity(desc="${responseData[index].opname} Commission ${responseData[index].comm}${responseData[index].type}"))
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
        binding.btnHasdata.setOnClickListener {
            loadMoreData()
        }
      /*  binding.nsv?.viewTreeObserver?.addOnScrollChangedListener {
            val scrollY = binding.nsv?.scrollY
            val scrollViewHeight = binding.nsv?.height
            val contentViewHeight = binding.nsv?.getChildAt(0)?.height
            if (scrollViewHeight != null && contentViewHeight != null) {
                // Check if the button is visible
                val isButtonVisible = scrollY?.plus(scrollViewHeight) ?: 0 < contentViewHeight

                if (isButtonVisible) {
                    loadMoreData()
                } else {
                    // Button is not visible
                    // Add your code here
                    // For example, disable the button
                    // btnHasData?.isEnabled = false
                }
            }
        }*/

        //val handler = Handler(Looper.getMainLooper())
        /*binding.recycleViewReport.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        loadMoreData()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // If you want to cancel the delayed runnable when the touch is released or canceled
                        handler.removeCallbacksAndMessages(null)
                    }
                }
                // Return true to consume the touch event
                return true
            }
        })*/
        /* binding.nsv.setOnScrollChangeListener { _, _, scrollY, _, _ ->
             val lastVisibleItemPosition =
                 (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
             val totalItemCount = commissionReportAdapter?.itemCount ?: 0

             if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading) {
                 // Load more data when the last item is visible
                 loadMoreData()
             }
         }*/

        // Initial data loading
        loadMoreData()
    }

    private fun loadMoreData() {
        /*if (SystemClock.elapsedRealtime() - lastClickTime1 < 5000) {
            return
        }*/
        //loader?.show()
        /*  if (isLoading) {
              return
          }
          isLoading = true*/

        // Increment the current page
        currentPage++

        // Calculate the start and end indices for the next page
        //val startIndex = currentPage * itemsPerPage
        //val endIndex = startIndex + itemsPerPage

        // Call your function to load more data using the new indices
        //getAllData(startIndex, endIndex)
        activity?.let {

            lifecycleScope.launch {
                getAllData2()
            }


        }
    }

    private fun getAllData2() {
        if (!(endIndex >= (commissionReportList.size - 1))) {
            for (index in commissionReportList.indices) {
                if (index >= startIndex && index <= endIndex) {
                    var items = commissionReportList[index]
                    items.apply {
                        commissionReportList2.add(CommissionReportData(opname, comm, type))
                    }

                }
            }

            commissionReportAdapter?.items = commissionReportList2
            commissionReportAdapter?.notifyDataSetChanged()

        } else {
            binding.btnHasdata.visibility = View.GONE
        }
        //delay(2000)
        loader?.dismiss()
        startIndex = endIndex + 1
        endIndex += 10
        //showrecycleView()
    }
    /*lifecycleScope.launch {
        var data = tableViewModel.getDataInRange(0, 10)
        data?.forEach{
            Log.d("TAGp", "showrecycleView: "+it?.desc)
        }
    }*/
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



