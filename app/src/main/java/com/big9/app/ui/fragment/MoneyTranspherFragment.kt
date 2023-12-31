package com.big9.app.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentMoneyTranspherBinding

import com.big9.app.ui.base.BaseFragment

class MoneyTranspherFragment : BaseFragment() {
    lateinit var binding: FragmentMoneyTranspherBinding
    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_transpher, container, false)
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

            activity?.let {act->
                btnSubmit.setOnClickListener{
                    if (viewModel?.MoneyTranspherValidation() == true) {
                        findNavController().navigate(R.id.action_moneyTranspherFragment_to_beneficiaryFragment)
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
        binding.apply {

        }

    }

    fun setObserver() {
        binding?.apply {
            viewModel?.mobileSendMoney?.observe(viewLifecycleOwner){
                viewModel?.sendMoneyVisibility?.value = it.length==10
                // need to check from api
            }
        }

    }


}