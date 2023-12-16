package com.epaymark.big9.ui.popup


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.epaymark.big9.R
import com.epaymark.big9.data.viewMovel.MyViewModel

import com.epaymark.big9.databinding.FragmentSuccessPopupBinding
import com.epaymark.big9.ui.base.PopUpFragment
import com.epaymark.big9.utils.`interface`.CallBack4


class SuccessPopupFragment(val callBack4: CallBack4) : PopUpFragment() {
    lateinit var binding: FragmentSuccessPopupBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_success_popup, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
//success_img
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {

        binding.apply {
            buttonDismiss.setOnClickListener{
                callBack4.getValue4("","","","")
                dismiss()
            }

          }
        }


    fun initView() {
        /*Glide.with(this)
            .asGif()
            .load(R.drawable.success_img)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imgSuccess)*/
        Glide.with(this)
            .asGif()
            .load(R.drawable.success_img)
            .error(R.drawable.ic_success) // Set the default image resource
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imgSuccess)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    fun setObserver() {
        binding.apply {

        }

    }


}