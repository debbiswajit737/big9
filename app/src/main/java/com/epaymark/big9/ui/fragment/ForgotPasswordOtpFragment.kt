package com.epaymark.big9.ui.fragment


import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.adapter.PhonePadAdapter3
import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentOtpForgotPasswordBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.SuccessPopupFragment
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.`interface`.CallBack4
import com.epaymark.big9.utils.`interface`.KeyPadOnClickListner
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class ForgotPasswordOtpFragment : BaseFragment() {
    lateinit var binding: FragmentOtpForgotPasswordBinding
    var keyPad = ArrayList<Int>()
    private var loader: Dialog? = null
    private val authViewModel: AuthViewModel by activityViewModels()
    private val myViewModel: MyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp_forgot_password, container, false)
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observer()
    }

    private fun observer() {
        authViewModel?.resetTPINResponseReceptLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()

                    it?.data?.Description?.let { msg ->
                        myViewModel.popup_message.value =msg
                    }

                    val successPopupFragment = SuccessPopupFragment(object :
                        CallBack4 {
                        override fun getValue4(
                            s1: String,
                            s2: String,
                            s3: String,
                            s4: String
                        ) {
                            findNavController().popBackStack()
                        }

                    })
                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)

                    authViewModel?.resetTPINResponseReceptLiveData?.value = null
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    authViewModel?.resetTPINResponseReceptLiveData?.value = null
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }

    fun initView() {
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
        cownDown()
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
            adapter= PhonePadAdapter3(keyPad,object : KeyPadOnClickListner {
                override fun onClick(item: Int) {

                    authViewModel.otp.value?.apply {
                        if (item<=9 ) {
                            if (this.length!=6) {

                                // binding.firstPinView.text=this
                                authViewModel.otp.value = "${this}${item}"
                                if(authViewModel.otp.value=="123456"){
                                    val (isLogin, loginResponse) = sharedPreff.getLoginData()
                                    if (isLogin) {
                                        loginResponse?.let { loginData ->
                                            authViewModel?.apply {

                                                val data = mapOf(
                                                    "userid" to loginData.userid
                                                )

                                                val gson = Gson()
                                                var jsonString = gson.toJson(data)
                                                loginData.AuthToken?.let {
                                                    authViewModel.resetTPINResponse(it, jsonString.encrypt())
                                                }
                                            }

                                        }
                                    }
                                    //findNavController().popBackStack()
                                   // Toast.makeText(requireContext(), "match", Toast.LENGTH_SHORT).show()
                                }

                                //binding.firstPinView.setText(authViewModel.otp.value)
                            }
                        }
                        else if(item==10){
                            //authViewModel.keyPadValue.value =""
                        }
                        else {
                            if (this.isNotEmpty()) {
                                authViewModel.otp.value = this.toString().substring(0, this.length - 1)
                                //binding.firstPinView.setText(authViewModel.otp.value)

                            }

                        }
                    }

                }

            })
            isNestedScrollingEnabled=false
        }


    }



    fun cownDown() {


        val totalTimeInMillis: Long = TimeUnit.SECONDS.toMillis(30) // 30 seconds countdown

       var countDownTimer= object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                //authViewModel.timingValue.value = getString(R.string.resend_otp,secondsRemaining)/*"Resend OTP after <font color='#B80A13'>$secondsRemaining</font> second"*/
                //binding.tdResendOtp.text = getString(R.string.resend_otp,secondsRemaining)/*"Resend OTP after <font color='#B80A13'>$secondsRemaining</font> second"*/
                setTimerValue(secondsRemaining)
            }

            override fun onFinish() {
                val spannableString = SpannableStringBuilder()
                //authViewModel.timingValue.value =  "Resend OTP"
                //binding.tdResendOtp.text =  "Resend OTP"
                val firstPart = "Resend OTP"
                spannableString.append(firstPart)
                spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.border_focus)),
                    spannableString.length - firstPart.length,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.tdResendOtp.text =spannableString
            }
        }


       countDownTimer.start()

    }

    private fun setTimerValue(secondsRemaining: Long) {
        // Create a SpannableStringBuilder
        val spannableString = SpannableStringBuilder()

        // Add the first part of the text with one color
        val firstPart = "Resend OTP after "
        spannableString.append(firstPart)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.border_focus)),
            spannableString.length - firstPart.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Add the second part of the text with a different color
        val secondPart = "$secondsRemaining"
        spannableString.append(secondPart)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.white)),
            spannableString.length - secondPart.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.append("s")
        // Set the SpannableStringBuilder as the text for the TextView
        binding.tdResendOtp.text = spannableString
    }
}