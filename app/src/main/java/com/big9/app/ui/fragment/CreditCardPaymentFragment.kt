package com.big9.app.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentCreditCardPaymentBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.google.gson.Gson

class CreditCardPaymentFragment : BaseFragment() {
    lateinit var binding: FragmentCreditCardPaymentBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_credit_card_payment, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {

            imgBack.back()
            btnSubmit.setOnClickListener{
                activity?.let {act->
                    if (viewModel?.creditValidation() == true){
                        val (isLogin, loginResponse) =sharedPreff.getLoginData()
                        if (isLogin){
                            loginResponse?.let {loginData->
                                viewModel?.apply {

                                   val  data = mapOf(
                                        "userid" to loginData.userid,
                                        "cardholdername" to credit_holder_name.value,
                                        "network" to select_card_type.value,
                                        "cardnumber" to credit_card.value,
                                        "mobile" to credit_mobile.value,
                                        "amount" to credit_amt.value,
                                        "remarks" to credit_remarks.value,
                                    )

                                    val gson= Gson()
                                    var jsonString = gson.toJson(data)
                                    loginData.AuthToken?.let {
                                        viewModel?.otp?.value=""

                                       creditSendVerifyOtp(it,jsonString.encrypt())
                                    }
                                }

                            }
                        }

                    }
                }

            }

        }



    }

    fun initView() {
        binding.apply {
            etAmt.setupAmount()
        }
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
    }

    fun setObserver() {
        binding.apply {
            etRemarks.oem(btnSubmit)
        }
        viewModel?.creditCardSendOtpResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                     loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    if (it.data?.responseCode==200){
                        viewModel?.apply {
                            creditCardID.value= it.data.ID.toString()
                            stateresp.value=it.data.stateresp
                        }
                       viewModel.fromPage="creditCard"
                       findNavController().navigate(R.id.action_creditCardPaymentFragment_to_transactionOtpFragment)
                    }
                    viewModel?.creditCardSendOtpResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    viewModel?.creditCardSendOtpResponseLiveData?.value=null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }


}