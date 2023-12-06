package com.epaymark.big9.ui.fragment


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
import com.epaymark.big9.databinding.FragmentAddBeneficiaryBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.epay.ui.popup.CustomPopup.showDebitPopup
import com.epaymark.epay.utils.`interface`.CallBack

class AddBeneficiaryFragment : BaseFragment() {
    lateinit var binding: FragmentAddBeneficiaryBinding
    private val viewModel: MyViewModel by activityViewModels()
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
                    showDebitPopup(tvVerify.context,object:CallBack{
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
                    })
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

        }
    }

    fun setObserver() {

    }


}