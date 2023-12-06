package com.epaymark.big9.ui.fragment.tablayout


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.epaymark.big9.R

import com.epaymark.big9.data.model.ContactModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentChangeTpinBinding

import com.epaymark.big9.ui.base.BaseFragment


class ChangeTPINPinFragment : BaseFragment() {
    lateinit var binding: FragmentChangeTpinBinding
    private val viewModel: MyViewModel by activityViewModels()
    var contactModelList = ArrayList<ContactModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_tpin, container, false)
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
            btnSubmit.setOnClickListener{
                viewModel?.changeLoginTPinValidation()
            }
          }
        }


    fun initView() {

    }

    fun setObserver() {
        binding.apply {

        }

    }




}