package com.epaymark.big9.ui.fragment.tablayout


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.data.model.ContactModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentChangeTpinBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.SuccessPopupFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.common.MethodClass.custom_loader
import com.epaymark.big9.utils.`interface`.CallBack4
import com.google.gson.Gson


class ChangeTPINPinFragment : BaseFragment() {
    lateinit var binding: FragmentChangeTpinBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_tpin, container, false)
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



    private fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
    }

    private fun onViewClick() {

        binding.apply {
            btnSubmit.setOnClickListener{
                if (viewModel?.changeLoginTPinValidation() == true){
                    changeTpinPassword()
                }
            }
        }

    }

    private fun changeTpinPassword() {
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        if (isLogin){
            loginResponse?.let {loginData->
                val data = mapOf(
                    "userid" to loginData.userid,
                    "new_tpin" to viewModel?.newTPin?.value,
                    "confirm_tpin" to viewModel?.confirmTPin?.value,
                    "old_tpin" to viewModel?.oldTPin?.value,

                    )
                val gson= Gson()

                var jsonString = gson.toJson(data)
                loginData.AuthToken?.let {


                    viewModel?.changeTPin(it, jsonString.encrypt())
                }

            }
        }
    }

    private fun observer() {
        viewModel.changeTPinResponseLiveData.observe(viewLifecycleOwner){
            when(it){
                is ResponseState.Loading -> {
                    loader?.show()
                }
                is ResponseState.Success->{
                    loader?.dismiss()
                    viewModel?.newPin?.value=""
                    viewModel?.confirmPin?.value=""
                    viewModel?.oldPin?.value=""
                    it.data?.let {
                        viewModel.popup_message.value=it.Description
                        val successPopupFragment = SuccessPopupFragment(object :
                            CallBack4 {
                            override fun getValue4(
                                s1: String,
                                s2: String,
                                s3: String,
                                s4: String
                            ) {
                                viewModel?.newTPin?.value=""
                                viewModel?.confirmTPin?.value=""
                                viewModel?.oldTPin?.value=""
                                findNavController().popBackStack()
                            }
                        })
                        successPopupFragment.show(childFragmentManager, successPopupFragment.tag)
                    }
                    loader?.dismiss()
                    viewModel.changeTPinResponseLiveData.value=null
                }
                is ResponseState.Error->{
                    loader?.dismiss()
                    viewModel?.newTPin?.value=""
                    viewModel?.confirmTPin?.value=""
                    viewModel?.oldTPin?.value=""

                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    viewModel.changeTPinResponseLiveData.value=null
                }
            }
        }
    }
}