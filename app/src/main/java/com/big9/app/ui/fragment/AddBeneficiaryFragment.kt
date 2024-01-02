package com.big9.app.ui.fragment



import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R
import com.big9.app.data.model.BankListModel


import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentAddBeneficiaryBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.ui.fragment.BankListBottomSheetDialog
import com.big9.app.ui.popup.CustomPopup.showDebitPopup
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.`interface`.CallBack
import com.google.gson.Gson


class AddBeneficiaryFragment : BaseFragment() {
    lateinit var binding: FragmentAddBeneficiaryBinding
    private val viewModel: MyViewModel by activityViewModels()
    var bankList = ArrayList<BankListModel>()
    private var loader: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_beneficiary, container, false)
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
                    if (viewModel?.beneficiaryValidation() == true){
                    findNavController().popBackStack()
                    }
                }

            }
            tvVerify.setOnClickListener{
                if (viewModel?.beneficiaryVerifyValidation() == true){
                    showDebitPopup(tvVerify.context,object: CallBack {
                        override fun getValue(s: String) {
                            // API call
                            viewModel?.beneficiary_name?.value="Sample Beneficiary Name"
                        }
                    })

                }
            }

            etBeneficiaryBankName.setOnClickListener{
                activity?.let {act->
                    val bankListBottomSheetDialog = BankListBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                            Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                        }
                    },bankList)
                    bankListBottomSheetDialog.show(
                        act.supportFragmentManager,
                        bankListBottomSheetDialog.tag
                    )
                }

            }




        }



    }

    fun initView() {
        binding.apply {

            activity?.let {act->
                        loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            }
            val (isLogin, loginResponse) =sharedPreff.getLoginData()
            loginResponse?.let { loginData ->
                loginData.userid?.let {
                    val data = mapOf(
                        "userid" to it,
                    )
                    val gson =  Gson();
                    var jsonString = gson.toJson(data);
                    loginData.AuthToken?.let {
                        viewModel?.addBankBankList(it,jsonString.encrypt())
                    }
                }
            }
        }
    }

    fun setObserver() {
        viewModel?.addBankBankListResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    bankList.clear()
                    it.data?.data?.let {
                        it.forEach{
                            bankList.add(BankListModel(R.drawable.default_1,it.name.toString(),"A/C",it.ifsc.toString()))
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