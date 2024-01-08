package com.big9.app.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.ReceiptModel
import com.big9.app.data.viewMovel.DTHViewModel

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentAddRetailerBinding
import com.big9.app.databinding.FragmentDthRechargeBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError
import com.big9.app.ui.activity.DashboardActivity

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.popup.RetailerSuccessPopupFragment
import com.big9.app.ui.popup.SuccessPopupFragment
import com.big9.app.ui.receipt.ElectricReceptDialogFragment
import com.big9.app.ui.receipt.newRecept.DTHnewMobileReceptDialogFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.common.MethodClass.userLogout
import com.big9.app.utils.helpers.Constants
import com.big9.app.utils.helpers.Constants.isDthOperator
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Objects

class RetailerPaymentFragment : BaseFragment() {
    lateinit var binding: FragmentAddRetailerBinding
    private val viewModel: MyViewModel by activityViewModels()
    private val dthViewModel: DTHViewModel by viewModels()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_retailer, container, false)
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
            rootView.setOnClickListener {
                activity?.let { act -> rootView.hideSoftKeyBoard(act) }
            }


            btnSubmit.setOnClickListener {
                if (viewModel?.mobileforRetailerValidation() == true) {
                    submit()
                }
            }


        }
    }


    fun initView() {

        activity?.let {act->
                    loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        backPressedCheck()


    }

    private fun submit() {
        val (isLogin, loginResponse) = sharedPreff.getLoginData()
        loginResponse?.let { loginData ->
            loginData.userid?.let {

                val data = mapOf(
                    "userid" to loginData.userid,
                    "mobile" to viewModel?.ratailerMobile?.value.toString()
                )
                val gson = Gson()
                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {
                    viewModel?.addRetailer(it, jsonString.encrypt())
                }
            }
        }
    }

    fun setObserver() {
        viewModel?.addRetailerResponseLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()


                    val successPopupFragment = RetailerSuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            context?.let { ctx->
                                ctx.userLogout()
                            }

                          }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                    //binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.addRetailerResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    //binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    viewModel?.addRetailerResponseLiveData?.value=null
                }
            }
        }
    }

    fun backPressedCheck() {}

}
