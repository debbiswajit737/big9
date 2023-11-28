package com.epaymark.big9.ui.fragment.paymentRequest


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.adapter.BankModeListAdapter
import com.epaymark.big9.data.model.BankModeListModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentPaymentModeBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.`interface`.CallBack

class PaymentRequestModeFragment : BaseFragment() {
    lateinit var binding: FragmentPaymentModeBinding
    private val viewModel: MyViewModel by activityViewModels()
    var bankInformationList = ArrayList<BankModeListModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_mode, container, false)
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

        }



    }

    fun initView() {
        binding.apply {
            //
            recycleViewPaymentMode.apply {
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"CASH - COUNTER DEPOSIT","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"CASH CDM","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE SAME BANK","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE - IMPS","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE - NEFT","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                bankInformationList.add(BankModeListModel(R.drawable.rounded_bank,"ONLINE - RTGS","Minimam Amount:100.00","Maximum Amount: 500000.00"))
                adapter= BankModeListAdapter(bankInformationList, object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.selectedBankMode?.value=s
                        findNavController().navigate(R.id.action_paymentRequestModeFragment_to_paymentRequestImformationFragment)
                    }

                })
            }
        }
    }

    fun setObserver() {

    }


}