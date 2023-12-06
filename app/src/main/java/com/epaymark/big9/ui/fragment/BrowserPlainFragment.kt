package com.epaymark.big9.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.adapter.BrowserAdapter
import com.epaymark.big9.data.model.BrowserModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.BrowserBottomsheetLayoutBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.epay.utils.`interface`.CallBack

class BrowserPlainFragment : BaseFragment() {
    lateinit var binding: BrowserBottomsheetLayoutBinding
    private val viewModel: MyViewModel by activityViewModels()
    var browser = ArrayList<BrowserModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.browser_bottomsheet_layout, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    private fun setObserver() {

    }

    private fun initView() {

        binding.recycleBrowser.apply {
            browser.clear()
            browser.add(BrowserModel("152","Truly Unlimited","1GB","SMS 300","20 days",false))
            browser.add(BrowserModel("199","Truly unlimited calls","1GB","SMS 100/day","180 days",false))
            browser.add(BrowserModel("1000","Truly Unlimited","1GB/day","SMS 300","54 days",false))
            browser.add(BrowserModel("2199","Truly unlimited calls","1GB","SMS 100/day","84 days",false))
            browser.add(BrowserModel("3000","Truly Unlimited","1GB","SMS 100","24 days",false))

            adapter= BrowserAdapter(browser, object : CallBack {
                override fun getValue(s: String) {
                    viewModel.amt.value=s
                    //callBack.getValue(s)
                    //dismiss()
                    findNavController().popBackStack()
                }

            })

        }
    }

}