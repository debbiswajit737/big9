package com.epaymark.big9.ui.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R
import com.epaymark.big9.adapter.BillerListAdapter
import com.epaymark.big9.data.model.ElectricModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentElectricRechargeBinding
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.`interface`.CallBack

class ElectricRechargeFragment : BaseFragment() {
    lateinit var binding: FragmentElectricRechargeBinding
    private val viewModel: MyViewModel by activityViewModels()
    var billerList = ArrayList<ElectricModel>()
    var billerListAdapter: BillerListAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_electric_recharge, container, false)
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
            billerList.clear()
            billerList.add(ElectricModel("West Bengal Electricity",R.drawable.wbsedcl,false))
            billerList.add(ElectricModel("CESC Limited",R.drawable.cesc,false))


            binding.recycleElectric.apply {

                billerListAdapter= BillerListAdapter(billerList,object : CallBack {
                    override fun getValue(s: String) {
                        viewModel?.billerAddress?.value=s
                        findNavController().navigate(R.id.action_electricRechargeFragment_to_utilityBillPaymentFragment)
                    }

                })
                adapter=billerListAdapter


                binding.etSearch.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        billerListAdapter?.filter?.filter(s)
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })
            }
        }

    }

    fun setObserver() {

    }


}