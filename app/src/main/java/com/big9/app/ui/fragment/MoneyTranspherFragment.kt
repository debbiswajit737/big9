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
import checkUserModel
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMoneyTranspherBinding
import com.big9.app.network.ResponseState
import com.big9.app.network.RetrofitHelper.handleApiError

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.big9.app.utils.helpers.Constants.bankCode
import com.big9.app.utils.helpers.Constants.customerId
import com.google.gson.Gson

class MoneyTranspherFragment : BaseFragment() {
    lateinit var binding: FragmentMoneyTranspherBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    var isNewUser=false
    var remiterUserData: checkUserModel? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_transpher, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel?.sendMoneyVisibility?.value=false
        viewModel?.mobileSendMoney?.value=""
        viewModel?.nameSendMoney?.value=""
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

            activity?.let {act->
                btnSubmit.setOnClickListener{
                    if (viewModel?.MoneyTranspherValidation() == true) {
                        if(viewModel?.sendMoneyVisibility?.value==true){
                            if (isNewUser){

                                    remiterUserData.apply {
                                        val bundle = Bundle()

                                            bundle.putString("customerid", customerId)
                                            bundle.putString("customer_number", viewModel?.mobileSendMoney?.value)
                                            bundle.putString("customer_name", viewModel?.nameSendMoney?.value)
                                            findNavController().navigate(R.id.action_moneyTranspherFragment_to_otpNewRmnFragment,bundle)


                                    }


                               // isNewUser=false

                            }
                            else {


                                findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
                            }
                          //  isNewUser=false
                        }
                        else {
                            val (isLogin, loginResponse) = sharedPreff.getLoginData()
                            if (isLogin) {
                                loginResponse?.let { loginData ->
                                    viewModel?.apply {

                                        val data = mapOf(
                                            "customer_number" to viewModel?.mobileSendMoney?.value,
                                            "userid" to loginData.userid,

                                            )

                                        val gson = Gson()
                                        var jsonString = gson.toJson(data)
                                        loginData.AuthToken?.let {
                                            val data = jsonString.encrypt()
                                            checkUser(it, data)
                                        }
                                    }

                                }
                            }
                        }





                        //findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
                        /*activity?.let {act->
                            val selectTransactionTypeBottomSheetDialog = SelectTransactionTypeBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {

                                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                                        override fun getValue(s: String) {
                                            Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                    tpinBottomSheetDialog.show(
                                        act.supportFragmentManager,
                                        tpinBottomSheetDialog.tag
                                    )


                                }
                            })
                            selectTransactionTypeBottomSheetDialog.show(
                                act.supportFragmentManager,
                                selectTransactionTypeBottomSheetDialog.tag
                            )
                        }

*/

                    }
                }

            }


        }
    }
    fun initView() {

        activity?.let {act->
         loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
        }
        binding.apply {

        }

    }

    fun setObserver() {
        binding?.apply {
            viewModel?.mobileSendMoney?.observe(viewLifecycleOwner){
              //  viewModel?.sendMoneyVisibility?.value = it.length==10
                // need to check from api
            }
        }


        // 
        viewModel?.checkUserResponseLiveData?.observe(viewLifecycleOwner) {
        when (it) {
        is ResponseState.Loading -> {
            loader?.show()

        }
        is ResponseState.Success -> {
            loader?.dismiss()
            remiterUserData=it.data

            remiterUserData?.response?.let {
                var bank1=0
                var bank2=0
                var bank3=0
               /* bank1

                "bank3_limit":25000,"bank3_status":"yes","bank2_limit":25000,"bank1_limit":25000}}*/
            }
            customerId=remiterUserData?.custID.toString()
            remiterUserData?.status?.let {status->

              if (status=="200"){
                  isNewUser=false
                  viewModel?.mobileSendMoney?.value=it.data?.response?.mobile.toString()
                  viewModel?.nameSendMoney?.value="${it.data?.response?.fname.toString()} ${it.data?.response?.lname.toString()}".replace("null","")




                 /* val bundle2 = Bundle()
                       var  castIdData= remiterUserData?.custID.toString()
                      bundle2.putString("customerid", castIdData)*/

                  findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
              }
               else   if (status.lowercase()=="201"){
                  isNewUser=false
                  viewModel?.sendMoneyVisibility?.value = true
                  binding.etMob.isFocusable = false
                  binding.etMob.isFocusableInTouchMode = false
              }
               else  if (status=="202"){
                  isNewUser=true
                  bankCode= remiterUserData?.stateresp.toString()
                  viewModel?.sendMoneyVisibility?.value = true
                  binding.etMob.isFocusable = false
                  binding.etMob.isFocusableInTouchMode = false
              }
            /*   isNewUser=true
               viewModel?.sendMoneyVisibility?.value = true
               binding.etMob.isFocusable = false
               binding.etMob.isFocusableInTouchMode = false*/
           }
            viewModel?.checkUserResponseLiveData?.value=null
        }
        is ResponseState.Error -> {
            loader?.dismiss()
            handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
            viewModel?.checkUserResponseLiveData?.value=null
        }

            else -> {}
        }
}

    }


}