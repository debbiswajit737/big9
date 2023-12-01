package com.epaymark.big9.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.epaymark.big9.R
import com.epaymark.big9.databinding.FragmentFormBinding
import com.epaymark.big9.ui.base.BaseFragment


class FormFragment : BaseFragment() {
    lateinit var binding: FragmentFormBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_form, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
    }

    fun initView() {

    }

    fun setObserver() {

    }
}