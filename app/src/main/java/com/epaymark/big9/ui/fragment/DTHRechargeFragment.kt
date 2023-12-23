package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R
import com.epaymark.big9.data.model.DTHOperatorData
import com.epaymark.big9.data.model.DTHTranspherModel
import com.epaymark.big9.data.viewMovel.DTHViewModel

import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentDthRechargeBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.SuccessPopupFragment
import com.epaymark.big9.ui.receipt.DthReceptDialogFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.helpers.Constants.isDthOperator
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.`interface`.CallBack4
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import java.util.Objects

class DTHRechargeFragment : BaseFragment() {
    lateinit var binding: FragmentDthRechargeBinding
    private val viewModel: MyViewModel by activityViewModels()
    private val dthViewModel: DTHViewModel by viewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dth_recharge, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initView()
            setObserver()
            onViewClick()
        }
    }

    private fun onViewClick() {
        binding.apply {

            imgBack.back()

            btnCustomerInfo.setOnClickListener{
                activity?.let {
                    loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

                    val (isLogin, loginResponse) =sharedPreff.getLoginData()
                    if (isLogin){
                        loginResponse?.let {loginData->
                            val data = mapOf(
                                "userid" to loginData.userid,
                                "operator" to viewModel?.operator?.value,
                                "opcode" to viewModel?.operatorCode?.value,
                                "subscriber_id" to viewModel?.subId?.value,
                                "opname" to viewModel?.dthOperator?.value,

                            )
                            val gson= Gson()

                                var jsonString = gson.toJson(data)
                            loginData.AuthToken?.let {
                                dthViewModel?.dthUserInfo(it, jsonString.encrypt())
                            }

                        }
                    }

                }

               /* context?.let {ctx->
                    viewModel?.userName?.value = "Sample user"
                    viewModel?.balence?.value = "1009"
                    viewModel?.nextRecharge?.value = "01-01-2023"
                    viewModel?.monthly?.value = "789"
                    viewModel?.type?.value = "Active"
                    binding.cardUserDetails.visibility=View.VISIBLE
                }*/
            }

            btnSubmit.setOnClickListener{
                if (viewModel?.dthValidation() == true){
                    btnSubmit.setBottonLoader(false,llSubmitLoader)
                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                            submit(s)

                        }
                    })
                    activity?.let {act->
                        tpinBottomSheetDialog.isCancelable = false
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                    }
                }
            }
            etOperator.setOnClickListener {
                rlOperator.performClick()
            }
            rlOperator.setOnClickListener{
                activity?.let {act->
                    isDthOperator=true
                    findNavController().navigate(R.id.action_DTHRechargeFragment_to_operatorFragment)
                }

            }
        }

        binding.imgClose.setOnClickListener{
            binding.cardUserDetails.visibility=View.GONE
        }

    }

    fun initView() {
        binding.apply {
            etAmt.setupAmount()
        }

    }
    private fun submit(tpin: String) {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            if (isLogin){
                loginResponse?.let {loginData->
                    val data ="{\"userid\":\"${loginData.userid}\",\"operator\":\"${viewModel.operator.value}\",\"opcode\":\"${viewModel.operatorCode.value}\",\"tpin\":\"${tpin}\",\"subscriber_id\":\"${viewModel.mobile.value}\",\"rcamt\":\"${viewModel.amt.value}\",\"IP\":\"${MethodClass.getLocalIPAddress()}\"}"
                    /*val data = mapOf(
                        "userid" to loginData.userid,
                        "operator" to viewModel.operator.value,
                        "opcode" to viewModel.operatorCode.value,
                        "tpin" to tpin,
                        "custno" to viewModel.mobile.value,
                        "rcamt" to viewModel.amt,
                        "IP" to getLocalIPAddress(),
                    )*/
                    /*val gson= Gson()

                        var jsonString = gson.toJson(data)*/
                    loginData.AuthToken?.let {
                            dthViewModel?.dthTransfer(it, data.encrypt())
                    }

                }
            }

        }
    }
    fun setObserver() {
        dthViewModel?.dthTransferResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()
                    viewModel?.popup_message?.value="Success"
                    val successPopupFragment = SuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            it.data?.let {
                                val dialogFragment = DthReceptDialogFragment(object: CallBack {
                                    override fun getValue(s: String) {
                                        if (Objects.equals(s,"back")) {
                                            findNavController().popBackStack()
                                        }
                                    }
                                },it)
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)
                            }
                        }
                    }
                    )
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)


                    viewModel?.apply {
                        subId.value=""
                        dthOperator.value=""
                        dthAmt.value=""
                    }
                    dthViewModel?.dthTransferResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    dthViewModel?.dthTransferResponseLiveData?.value=null
                }
            }
        }

        dthViewModel?.dthUserInfoResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    context?.let { ctx ->
                        it?.data?.data?.get(0)?.let {


                        viewModel?.userName?.value = it.customerName
                                viewModel?.balence?.value = it.Balance
                        viewModel?.nextRecharge?.value = it.NextRechargeDate
                        viewModel?.monthly?.value = it.MonthlyRecharge
                        viewModel?.type?.value = it.status
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.cardUserDetails.visibility = View.VISIBLE
                        },100)

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


}