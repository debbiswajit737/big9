package com.epaymark.big9.ui.fragment.paymentRequest


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentPaymentRequestImformationBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.fragment.CameraDialog
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.Constants.isGallary
import com.epaymark.big9.utils.helpers.Constants.isIsPaySlip
import com.epaymark.big9.utils.helpers.Constants.isVideo
import com.epaymark.big9.utils.`interface`.CallBack

class PaymentRequestImformationFragment : BaseFragment() {
    lateinit var binding: FragmentPaymentRequestImformationBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var authViewModel: AuthViewModel?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_request_imformation, container, false)
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

    override fun onResume() {
        super.onResume()
        if (isIsPaySlip){
            authViewModel?.filePath?.observe(viewLifecycleOwner){
                binding.imgPlaySlip.setImage(it)
            }

            isIsPaySlip=false
        }
    }
    private fun onViewClick() {
        binding.apply {

            imgBack.back()
            imgPlaySlip.setOnClickListener{tvUploadPayslip.performClick()}
            tvUploadPayslip.setOnClickListener{
                activity?.let {act->
                    Constants.isBackCamera =true

                    Constants.isPdf =false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {
                            getImage(s)
                        }

                    },false)
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)

                }
                }
            tvDate.setOnClickListener {
                it.showDatePickerDialog(object : CallBack {
                    override fun getValue(s: String) {
                        //tvDob.text = s
                        viewModel?.depositeDate?.value=s
                        viewModel?.depositeDateErrorVisible?.value =false
                        viewModel?.depositeDateError?.value=""
                    }

                })
            }
            btnSubmit.setOnClickListener{
                if (viewModel?.PaymentrequestValidation() == true){
                    Toast.makeText(requireActivity(), "Ok", Toast.LENGTH_SHORT).show()
                }
            }
        }



    }

    private fun getImage(s:String) {
        when(s){
            "g"->{
                isVideo =false
                isGallary =true
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                //findNavController().navigate(R.id.action_regFragment_to_cameraFragment)
            }
            "t"->{
                isIsPaySlip=true
                isVideo =false
                isGallary =false
                findNavController().navigate(R.id.action_paymentRequestImformationFragment_to_cameraFragment2)
            }

        }
    }

    fun initView() {
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        binding.apply {
            etAmt.setupAmount()
        }
    }

    fun setObserver() {

    }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            authViewModel?.filePath?.value = uri
            binding.imgPlaySlip.setImage(uri)
            /*Glide.with(binding.tvUploadPayslip.context)
                .load(uri)
                .into(binding.imgPlaySlip)*/
            //findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        } else {
            viewModel?.filePath?.value = Uri.parse("/")

            Log.d("PhotoPicker", "No media selected")
        }

    }


}