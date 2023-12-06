package com.epaymark.epay.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.adapter.PhonePadAdapter
import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.databinding.FragmentLoginBinding


import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.epay.utils.`interface`.KeyPadOnClickListner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    lateinit var binding: FragmentLoginBinding
    var keyPad = ArrayList<Int>()
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onViewClick()
        setObserver()
    }

    fun initView() {

        var a="abc".encrypt()
        Log.d("TAG_edata", "encript data : "+a)
        val b=a.decrypt()
        Log.d("TAG_edata", "decript data : "+b)

        keyPad.add(1)
        keyPad.add(2)
        keyPad.add(3)
        keyPad.add(4)
        keyPad.add(5)
        keyPad.add(6)
        keyPad.add(7)
        keyPad.add(8)
        keyPad.add(9)
        keyPad.add(10)
        keyPad.add(0)
        keyPad.add(11)
        binding.recyclePhonePad.apply {
            //authViewModel.keyPadValue.value=getString(R.string.mobile_no_hint)
            adapter= PhonePadAdapter(keyPad,object : KeyPadOnClickListner{
                override fun onClick(item: Int) {
                    authViewModel.mobError.value=""
                    authViewModel.keyPadValue.value?.apply {
                        if (item<=9 ) {
                            if (this.length!=10) {
                                authViewModel.keyPadValue.value = "${this}$item"
                            }
                        }
                        else if(item==10){
                            //authViewModel.keyPadValue.value =""
                        }
                        else {
                            if (this.isNotEmpty()) {
                                authViewModel.keyPadValue.value = this.substring(0, this.length - 1)
                            }

                        }
                    }

                }

            })
            isNestedScrollingEnabled=false
        }
    }

    private fun onViewClick() {
        binding.apply {
            btnConfirmLocation.setOnClickListener {
                if (authViewModel.keyPadValue.value?.isEmpty() == true || authViewModel.keyPadValue.value?.length!=10){
                    authViewModel.mobError.value="Please enter a valid mobile number."
                }
                else{

                    authViewModel.mobError.value=""
                    viewModel?.keyPadValue?.value?.let {
                        /*val loginModel=LoginModel(authData=it)
                        val gson= Gson()
                        val jsonString = gson.toJson(loginModel)
                        viewModel.authLoginRegistration(jsonString.encrypt())*/
                    }

                    findNavController().navigate(R.id.action_loginFragment_to_otpFragment)
                }
            }
        }

    }

    fun setObserver() {

    }
}