package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R
import com.epaymark.big9.data.model.PrepaidMoboleTranspherModel
import com.epaymark.big9.data.model.allReport.PostPaidMobileTranspherModel
import com.epaymark.big9.data.viewMovel.MobileRechargeViewModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentMobileRechargeBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.receipt.MobileReceptDialogFragment
import com.epaymark.big9.ui.receipt.PostPaidMobileReceptDialogFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.common.MethodClass.getLocalIPAddress
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.Constants.isDthOperator
import com.epaymark.big9.utils.helpers.Constants.isFirstPageOpeenPostPaidMobile
import com.epaymark.big9.utils.`interface`.CallBack
import java.util.Objects


class MobileRechargeFragment : BaseFragment() {
    lateinit var binding: FragmentMobileRechargeBinding
    private val viewModel: MyViewModel by activityViewModels()
    //private val mobileRechargeViewModel: MobileRechargeViewModel?=null
    private val mobileRechargeViewModel: MobileRechargeViewModel by viewModels()
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
        mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.value=null
        setObserver()
        onViewClick()
    }


    private fun onViewClick() {
        binding.apply {

            //imgBack.back()
            imgBack.setOnClickListener{
                findNavController().navigate(R.id.action_mobileRechargeFragment_to_homeFragment2)
            }
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
                if (viewModel?.mobileValidation() == true){
                    activity?.let { act ->
                        findNavController().navigate(R.id.action_mobileRechargeFragment_to_browserPlainFragment)
                        //browserPlainDialog.show(act.supportFragmentManager, browserPlainDialog.tag)
                    }
                }
            }

            btnSubmit.setOnClickListener{
                activity?.let {act->
                    if (viewModel?.regValidation() == true){
                        btnSubmit.setBottonLoader(false,llSubmitLoader)
                            val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                                override fun getValue(s: String) {
                                    submit(s)

                                }
                            })
                            tpinBottomSheetDialog.isCancelable=false
                            tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)

                    }
                }

            }

        }



    }

    private fun submit(tpin: String) {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))

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
                                if (viewModel?.prepaitOrPostPaid?.value== Constants.Postpaid) {
                                    mobileRechargeViewModel?.PostPaidMobileTranspher(it, data.encrypt())
                                }
                                else if (viewModel?.prepaitOrPostPaid?.value== Constants.Prepaid) {

                                    mobileRechargeViewModel?.PrePaidMobileTranspher(it, data.encrypt())
                                }
                                //loader?.show()
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

        mobileRechargeViewModel?.postPaidMobileTranspherResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()

                    val data= PostPaidMobileTranspherModel()
                    it.data?.let {
                        data?.amount=it.amount
                        data?.mobileno=it.mobileno
                        data?.curramt=it.curramt
                        data?.refillid=it.refillid
                        data.image=viewModel?.selectrdOperator?.value
                    }
                    val dialogFragment = PostPaidMobileReceptDialogFragment(object: CallBack {
                                    override fun getValue(s: String) {
                                        if (Objects.equals(s,"back")) {
                                            findNavController().popBackStack()
                                        }
                                    }
                                }, data)
                                dialogFragment.show(childFragmentManager, dialogFragment.tag)
                    mobileRechargeViewModel?.postPaidMobileTranspherResponseLiveData?.value=null

                }

                is ResponseState.Error -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    mobileRechargeViewModel?.postPaidMobileTranspherResponseLiveData?.value=null
                }
            }
        }
        mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()

                        val data= PrepaidMoboleTranspherModel()
                        it.data?.let {
                            data?.amount=it.amount
                            data?.mobileno=it.mobileno
                            data?.curramt="${it.curramt}"
                            data?.refillid=it.refillid
                            data.image=viewModel?.selectrdOperator?.value
                        }

                        val dialogFragment = MobileReceptDialogFragment(object: CallBack {
                            override fun getValue(s: String) {
                                if (Objects.equals(s,"back")) {
                                    findNavController().popBackStack()
                                }
                            }
                        }, data)
                        dialogFragment.show(childFragmentManager, dialogFragment.tag)


                    mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.value=null
                }

                is ResponseState.Error -> {
                    binding.btnSubmit.setBottonLoader(true,binding.llSubmitLoader)
                    loader?.dismiss()

                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                    mobileRechargeViewModel?.prePaidMobileTranspherResponseLiveData?.value=null

                }
            }
        }
    }


}