package com.epaymark.big9.ui.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.adapter.AdminBankListAdapter
import com.epaymark.big9.data.model.AdminBankListModel
import com.epaymark.big9.data.model.UserDetails
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentMinistatementFormBinding
import com.epaymark.big9.ui.base.BaseFragment

import com.epaymark.big9.ui.receipt.MiniStatementReceptDialogFragment
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.`interface`.CallBack4
import java.util.Objects

class MiniStatementFormFragment : BaseFragment() {
    lateinit var binding: FragmentMinistatementFormBinding
    private val viewModel: MyViewModel by activityViewModels()
    var userDetailsList = ArrayList<UserDetails>()
    var bankList = ArrayList<AdminBankListModel>()
    var adminBankListAdapter:AdminBankListAdapter?=null
    var selectedBank:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ministatement_form, container, false)
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
            btnBalenceEnquary.setOnClickListener {
                if (viewModel?.balenceValidation() == true){
                    if (selectedBank.isNotEmpty()) {
                        activity?.let { act ->
                            val aadharAuthBottomSheetDialog =
                                AadharAuthBottomSheetDialog(object : CallBack {
                                    override fun getValue(s: String) {
                                        //findNavController().navigate(R.id.action_miniStatementFormFragment_to_miniStatementFragment)
                                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                                            override fun getValue(s: String) {
                                                val dialogFragment = MiniStatementReceptDialogFragment(object: CallBack {
                                                    override fun getValue(s: String) {
                                                        if (Objects.equals(s,"back")) {
                                                            findNavController().popBackStack()
                                                        }
                                                    }
                                                })
                                                dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                            }
                                        })
                                        activity?.let {act->
                                            tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                                        }
                                    }
                                })
                            aadharAuthBottomSheetDialog.show(
                                act.supportFragmentManager,
                                aadharAuthBottomSheetDialog.tag
                            )
                        }
                    }
                    else{
                        Toast.makeText(requireActivity(), "Select Bank", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
    }

    fun initView() {
        binding.apply {
            binding.recycleViewBankList.apply {
                bankList.clear()
                bankList.add(AdminBankListModel(R.drawable.axix_bank_logo,"AXIX Bank",false))
                bankList.add(AdminBankListModel(R.drawable.icici,"ICICI Bank",false))
                adminBankListAdapter= AdminBankListAdapter(bankList, object : CallBack4 {
                    override fun getValue4(s1: String, s2: String, s3: String, s4: String) {
                        selectedBank=s1
                    }
                })
                adapter=adminBankListAdapter
            }


        }
    }

    fun setObserver() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adminBankListAdapter?.filter?.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }


}