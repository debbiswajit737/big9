package com.epaymark.big9.ui.fragment


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
import com.epaymark.big9.databinding.FragmentAddBankBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.Constants.isIsCheck
import com.epaymark.big9.utils.`interface`.CallBack

class AddBankFragment : BaseFragment() {
    lateinit var binding: FragmentAddBankBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var authViewModel: AuthViewModel?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bank, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.bank_check_ErrorVisible.value=false
        if (isIsCheck){
            authViewModel?.filePath?.observe(viewLifecycleOwner){
                binding.imgCheck.setImage(it)
            }
            isIsCheck =false
        }
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



            etAmt.oem(btnSubmit)
            btnSubmit.setOnClickListener{
                activity?.let { act ->
                    viewModel?.apply {
                    if (beneficiaryValidation()) {
                        if (authViewModel?.filePath?.value != null && authViewModel?.filePath?.value.toString() != "/") {
                            bank_check_ErrorVisible.value = false
                            findNavController().popBackStack()
                        } else {
                            hideKeyBoard(etAmt)
                            bank_check_ErrorVisible.value = true
                        }
                    }
                }
                }

            }


            etBeneficiaryBankName.setOnClickListener{
                activity?.let {act->
                    val bankListBottomSheetDialog = BankListBottomSheetDialog(object : CallBack {
                        override fun getValue(s: String) {
                            Toast.makeText(requireActivity(), "$s", Toast.LENGTH_SHORT).show()
                        }
                    })
                    bankListBottomSheetDialog.show(
                        act.supportFragmentManager,
                        bankListBottomSheetDialog.tag
                    )
                }

            }

            imgCheck.setOnClickListener{tvUploadPayslip.performClick()}
            tvUploadPayslip.setOnClickListener{
                activity?.let {act->
                    Constants.isBackCamera =true

                    Constants.isPdf =false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {
                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)

                }
            }
        }



    }

    fun initView() {
        binding.apply {
            authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        }
    }

    fun setObserver() {

    }

    private fun getImage(s:String) {
        when(s){
            "g"->{
                Constants.isVideo =false
                Constants.isGallary =true
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                //findNavController().navigate(R.id.action_regFragment_to_cameraFragment)
            }
            "t"->{
                Constants.isIsCheck =true
                Constants.isVideo =false
                Constants.isGallary =false
                findNavController().navigate(R.id.action_addBankFragment_to_cameraFragment2)
            }

        }
    }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            authViewModel?.filePath?.value = uri
            binding.imgCheck.setImage(uri)
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