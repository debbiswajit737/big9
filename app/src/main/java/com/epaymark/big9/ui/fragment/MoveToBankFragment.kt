package com.epaymark.big9.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.adapter.AccountDetailsAdapter
import com.epaymark.big9.data.model.AccountDetailsModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentMoveToBankBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.epay.ui.popup.SuccessPopupFragment
import com.epaymark.epay.ui.receipt.MoveToBankReceptDialogFragment
import com.epaymark.epay.utils.`interface`.CallBack
import com.epaymark.epay.utils.`interface`.CallBack4
import java.util.Objects


class MoveToBankFragment : BaseFragment() {
    lateinit var binding: FragmentMoveToBankBinding
    private val viewModel: MyViewModel by activityViewModels()
    var accountDetailsList = ArrayList<AccountDetailsModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_move_to_bank, container, false)
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
            fabAddBank.setOnClickListener{
                findNavController().navigate(R.id.action_moveToBankFragment_to_addBankFragment)
            }
          }
        }


    fun initView() {

        binding.recycleViewBankDetails.apply {
            accountDetailsList.clear()
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",false,"0087200100008770","PUNB00389600"))
            /*accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))
            accountDetailsList.add(AccountDetailsModel("Test User",true,"0087200100008770","PUNB00389600"))*/
            adapter= AccountDetailsAdapter(accountDetailsList,object:CallBack{
                override fun getValue(s: String) {
                    activity?.let {act->
                        val selectTransactionTypeBottomSheetDialog = SelectTransactionTypeBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {


                                            val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                                                override fun getValue(s: String) {

                                                    val successPopupFragment = SuccessPopupFragment(object :
                                                        CallBack4 {
                                                        override fun getValue4(
                                                            s1: String,
                                                            s2: String,
                                                            s3: String,
                                                            s4: String
                                                        ) {
                                                            val dialogFragment = MoveToBankReceptDialogFragment(object: CallBack {
                                                                override fun getValue(s: String) {
                                                                    if (Objects.equals(s,"back")) {
                                                                        findNavController().popBackStack()
                                                                    }
                                                                }
                                                            })
                                                            dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                                        }

                                                    })
                                                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)




                                                }
                                            })
                                            activity?.let {act->
                                                tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)
                                            }
                                            /*viewModel.receiveStatus.value="Failed"
                                            recycleViewReceiptList.clear()
                                            recycleViewReceiptList.add(ReceiptModel("Transaction Id","300000025", type = 4))
                                            recycleViewReceiptList.add(ReceiptModel("Subscriber/ Customer Number","8583863153", type = 1))
                                            recycleViewReceiptList.add(ReceiptModel("Transaction Amount","₹10.00", type = 2))
                                            recycleViewReceiptList.add(ReceiptModel("Running Balance","₹200.22", type = 3))
                                            recycleViewReceiptList.add(ReceiptModel("Operator","AIRTEL", type = 4))
                                            recycleViewReceiptList.add(ReceiptModel("Operator ID","N/A", type = 1))*/
                                            //ReceiptModel(val property:String, val reportValue:String, val title:String="", val price:String="", val transactionMessage:String="", val transactionId:String="", val userName:String="", val rrnId:String="", val type:Int=1)
                                            //val dialogFragment = ReceptDialogFragment()
                                            //dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                            //Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()



                            }
                        })
                        selectTransactionTypeBottomSheetDialog.show(
                            act.supportFragmentManager,
                            selectTransactionTypeBottomSheetDialog.tag
                        )
                    }

                }

            })
        }
    }

    fun setObserver() {
        binding.apply {

        }

    }


}