package com.epaymark.big9.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.epaymark.big9.R
import com.epaymark.big9.adapter.BankListAdapter
import com.epaymark.big9.data.model.BankListModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.BankListBottomsheetLayoutBinding
import com.epaymark.big9.ui.base.BaseBottomSheetFragment
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.`interface`.CallBack4

class BankListBottomSheetDialog(val callBack: CallBack) :BaseBottomSheetFragment() {
    lateinit var binding: BankListBottomsheetLayoutBinding
    private val myViewModel: MyViewModel by activityViewModels()
    var bankList = ArrayList<BankListModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bank_list_bottomsheet_layout, container, false)
        binding.viewModel = myViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun getTheme(): Int {
        return R.style.SheetDialog
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {}

    }

    private fun setObserver() {

    }

    private fun initView() {
        binding.apply {
            binding.apply {
                recycleViewPaymentRequest.apply {
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    bankList.add(BankListModel(R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
                    adapter= BankListAdapter(bankList, object : CallBack4 {
                        override fun getValue4(s1: String, s2: String, s3: String, s4: String) {
                            viewModel?.beneficiary_bank_name?.value=s1
                            viewModel?.beneficiary_ifsc?.value=s2
                            dismiss()
                        }




                    })
                }
            }
        }

    }


}