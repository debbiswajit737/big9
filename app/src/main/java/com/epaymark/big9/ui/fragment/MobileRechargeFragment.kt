package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentMobileRechargeBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.activity.RegActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.receipt.MobileReceptDialogFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.common.MethodClass.getLocalIPAddress
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.Constants.isDthOperator
import com.epaymark.big9.utils.helpers.Constants.isFirstPageOpeenPostPaidMobile
import com.epaymark.big9.utils.`interface`.CallBack
import com.google.gson.Gson
import java.util.Objects


class MobileRechargeFragment : BaseFragment() {
    lateinit var binding: FragmentMobileRechargeBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, container, false)
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
            viewModel?.prepaitIsActive?.value=true
            llPostPaid.setOnClickListener{
                viewModel?.prepaitIsActive?.value=false
                viewModel?.postpaitIsActive?.value=true
            }

            llPrePaid.setOnClickListener{
                viewModel?.prepaitIsActive?.value=true
                viewModel?.postpaitIsActive?.value=false
            }
            tvOperator.setOnClickListener{
                /*val operatorDialog = OperatorFragment(object : CallBack {
                    override fun getValue(s: String) {
                       viewModel?.operator?.value=s
                    }
                })*/
                activity?.let {act->
                    isDthOperator =false
                    findNavController().navigate(R.id.action_mobileRechargeFragment_to_operatorFragment)
                    //operatorDialog.show(act.supportFragmentManager, operatorDialog.tag)
                }

            }
            btnBrowse.setOnClickListener{
                /*val browserPlainDialog = BrowserPlainFragment(object : CallBack {
                    override fun getValue(s: String) {
                       viewModel?.operator?.value=s
                    }
                })*/

                    activity?.let { act ->
                        findNavController().navigate(R.id.action_mobileRechargeFragment_to_browserPlainFragment)
                        //browserPlainDialog.show(act.supportFragmentManager, browserPlainDialog.tag)

                }

            }

            btnSubmit.setOnClickListener{
                activity?.let {act->
                    if (viewModel?.regValidation() == true){

                            val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {
                                    submit(s)

                                }
                            })
                            tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)





                    }
                }

            }

        }



    }

    private fun submit(tpin: String) {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
            if (viewModel?.prepaitOrPostPaid?.value== Constants.Postpaid){
                val (isLogin, loginResponse) =sharedPreff.getLoginData()
                if (isLogin){
                    loginResponse?.let {loginData->
                        val data ="{\"userid\":\"${loginData.userid}\",\"operator\":\"${viewModel.operator.value}\",\"opcode\":\"${viewModel.operatorCode.value}\",\"tpin\":\"${tpin}\",\"custno\":\"${viewModel.mobile.value}\",\"rcamt\":\"${viewModel.amt.value}\",\"IP\":\"${getLocalIPAddress()}\"}"
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
                                viewModel?.PostPaidMobileTranspher(it,data.encrypt())
                                //loader?.show()
                            }

                    }
                }
            }
        }
    }

    fun initView() {
        binding.apply {
            activity?.let {
                loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
            }
            etAmt.setupAmount()
            if (viewModel?.prepaitOrPostPaid?.value== Constants.Postpaid) {
                binding.btnBrowse.visibility=View.GONE
            }
            else if (viewModel?.prepaitOrPostPaid?.value== Constants.Prepaid) {
                binding.btnBrowse.visibility=View.VISIBLE
            }
            if (isFirstPageOpeenPostPaidMobile) {
                isFirstPageOpeenPostPaidMobile = false
                activity?.let { act ->
                    isDthOperator = false
                    findNavController().navigate(R.id.action_mobileRechargeFragment_to_operatorFragment)
                    //operatorDialog.show(act.supportFragmentManager, operatorDialog.tag)
                }
            }
            //viewModel?.minMobileLength.value

        }
    }

    fun setObserver() {
        viewModel?.postPaidMobileTranspherResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                    val dialogFragment = MobileReceptDialogFragment(object: CallBack {
                                    override fun getValue(s: String) {
                                        if (Objects.equals(s,"back")) {
                                            findNavController().popBackStack()
                                        }
                                    }
                                })
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }


}