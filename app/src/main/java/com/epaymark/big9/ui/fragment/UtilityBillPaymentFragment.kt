package com.epaymark.big9.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.epaymark.big9.R

import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentUtilityBillPaymentBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.receipt.ElectricReceptDialogFragment
import com.epaymark.big9.utils.`interface`.CallBack
import java.util.Objects

class UtilityBillPaymentFragment : BaseFragment() {
    lateinit var binding: FragmentUtilityBillPaymentBinding
    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_utility_bill_payment, container, false)
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

            btnFetchAmt.setOnClickListener{
                activity?.let {act->
                    findNavController().navigate(R.id.action_utilityBillPaymentFragment_to_electricPriceListFragment)
                    /*val electricPriceListDialog = ElectricPriceListFragment(object : CallBack {
                        override fun getValue(s: String) {
                            viewModel?.consumerIdPrice?.value=s

                        }

                    })
                    electricPriceListDialog.show(act.supportFragmentManager, electricPriceListDialog.tag)
*/
                }

            }

            btnSubmit.setOnClickListener{
                if (viewModel?.electricValidation() == true){
                    val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                            val dialogFragment = ElectricReceptDialogFragment(object: CallBack {
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
            }

        }



    }

    fun initView() {
        binding.apply {
            Glide.with(binding.root.context)
                .asGif()
                .load(R.drawable.electric_light_animation_logo)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgGif)
        }

    }

    fun setObserver() {

    }


}