package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentEpotlyBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.SuccessPopupFragment
import com.epaymark.big9.ui.receipt.EPotlyReceptDialogFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.`interface`.CallBack4
import com.google.gson.Gson
import java.util.Objects

class EpotlyFragment : BaseFragment() {
    lateinit var binding: FragmentEpotlyBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_epotly, container, false)
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
                    if (viewModel?.epotlyValidation() == true){
                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                val (isLogin, loginResponse) =sharedPreff.getLoginData()
                                if (isLogin){
                                    loginResponse?.let {loginData->
                                        viewModel?.apply {

                                            val  data = mapOf(
                                                "userid" to loginData.userid,
                                                "tpin" to s,
                                                "custno" to epotly_mobile.value,
                                                "amt" to epotly_amt.value
                                            )

                                            val gson= Gson()
                                            var jsonString = gson.toJson(data)
                                            loginData.AuthToken?.let {
                                                epotlyTranspher(it,jsonString.encrypt())
                                            }
                                        }

                                    }
                                }



                            }
                        })
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)

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
        viewModel?.epotlyTranspherResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    viewModel.popup_message.value="${it?.data?.epotlyData?.status}\nMobile No.:${it?.data?.epotlyData?.mobileNo}\nLast Transaction Amount: ${it?.data?.epotlyData?.LastTransactionAmount}\nBalance : ${it?.data?.epotlyData?.curramt}"
                        val successPopupFragment = SuccessPopupFragment(object :
                            CallBack4 {
                            override fun getValue4(
                                s1: String,
                                s2: String,
                                s3: String,
                                s4: String
                            ) {
                                viewModel.popup_message.value="Success"
                                val dialogFragment = EPotlyReceptDialogFragment(object:
                                    CallBack {
                                    override fun getValue(s: String) {
                                        if (Objects.equals(s,"back")) {
                                            findNavController().popBackStack()
                                        }
                                    }
                                }, it?.data?.epotlyData, sharedPreff.getUserData())
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)

                            }

                        })
                        successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)


                }
            }
        }
    }


}