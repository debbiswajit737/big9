package com.epaymark.big9.ui.fragment.regandkyc



import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.R

import com.epaymark.big9.adapter.PhonePadAdapter
import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.databinding.FragmentLoginMobileBinding

import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.activity.AuthenticationActivity
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.LoadingPopup
import com.epaymark.big9.utils.helpers.Constants.API_KEY
import com.epaymark.big9.utils.helpers.Constants.CLIENT_ID

import com.epaymark.big9.utils.`interface`.KeyPadOnClickListner

import com.google.gson.Gson
import kotlin.random.Random


class LoginMobileFragment : BaseFragment() {
    lateinit var binding: FragmentLoginMobileBinding
    var keyPad = ArrayList<Int>()
    var loadingPopup: LoadingPopup? = null
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_mobile, container, false)
        binding.viewModel=authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setKeyPad(binding.recyclePhonePad)
        onViewClick()
        setObserver()

       /* var jsonString="G0nJq3v8G2BEzY/5KRWbqppwDkw3e/YvN3b3KxY6hhqHZq0z4cxOt8QWAe+rOxzs8uEI5vgZetmbz4R6G2wP+vWbeZ9dOWFWNWaX+FAm5KFd2sdAoAmoYeX+7K5goOHaPkX6LizHGQWLienTnY6GYM2powYi6um2615Ejs/lzrspKwDeAm0xfSVZjhABcYA5"

        val key = "a22786308b71488790be222216260e0a"
        val iv = "656dbf654a5dc"
        val jsondata = jsonString*/

// Encrypt
        /*val encryptedText = AesEncryptionUtil.encrypt(jsondata, key, iv)
        println("Encrypted Text: $encryptedText")*/

// Decrypt
        /*val decryptedText = AesEncryptionUtil.decrypt(jsondata, key, iv)
        println("Decrypted Text: $decryptedText")*/
    }

    private fun setObserver() {
        authViewModel?.authLogin?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loadingPopup?.show()
                }

                is ResponseState.Success -> {
                  //  loadingPopup?.dismiss()
                    val bundle=Bundle()
                    bundle.putBoolean("isForgotPin",false)

                    it?.data?.data?.let {loginResponse->
                        try {
                            //if (loginResponse.UserType!=null && (loginResponse.UserType=="R" || loginResponse.UserType=="D" || loginResponse.UserType=="SD" )) {

                                loginResponse.beforeLogin?.let {
                                    if (it.toInt() > 6) {

                                        sharedPreff?.setLoginData(loginResponse, true, "INACTIVE")
                                        findNavController().navigate(
                                            R.id.action_loginMobileFragment_to_otpMobileFragment,
                                            bundle
                                        )
                                    } else {
                                        if (loginResponse.userStatus?.trim() == "INACTIVE") {
                                            sharedPreff?.setLoginData(
                                                loginResponse,
                                                true,
                                                "INACTIVE"
                                            )
                                            if (loginResponse.kycstep != null) {
                                                startActivity(
                                                    Intent(
                                                        requireActivity(),
                                                        AuthenticationActivity::class.java
                                                    )
                                                )
                                            } else {
                                                findNavController().navigate(
                                                    R.id.action_loginMobileFragment_to_otpMobileFragment,
                                                    bundle
                                                )
                                            }
                                        } else if (loginResponse.userStatus?.trim() == "ACTIVE") {
                                            sharedPreff?.setLoginData(loginResponse, true, "ACTIVE")
                                            startActivity(
                                                Intent(
                                                    requireActivity(),
                                                    DashboardActivity::class.java
                                                )
                                            )
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "You are not valid user",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            /*}
                            else{
                                Toast.makeText(requireContext(), "You are not valid user", Toast.LENGTH_SHORT).show()
                            }*/

                        }catch (e:Exception){
                            Toast.makeText(requireContext(), ""+e.message, Toast.LENGTH_SHORT).show()
                        }

                    }

                }

                is ResponseState.Error -> {
                 //   loadingPopup?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }

    private fun onViewClick() {
        binding.apply {
            btnConfirmLocation.setOnClickListener {
                authViewModel.mobError.value=""
                if (viewModel?.keyPadValue?.value?.length==10){

                    viewModel?.keyPadValue?.value?.let {
                        loadingPopup?.show()

                        //"9356561988"
                        val data = mapOf(
                            "clientid" to CLIENT_ID,
                            "secretkey" to API_KEY,
                            "mobile" to it,
                            "refid" to "big9"+generateRandomNumberInRange().toString()
                        )
                        val gson= Gson()
                        var jsonString = gson.toJson(data)
                        Log.d("TAG_p", "AUTHcallProfile:json "+jsonString)
                        Log.d("TAG_p", "AUTHcallProfile:e \n"+jsonString.encrypt())
                        viewModel?.authLoginRegistration(jsonString.encrypt())
                    }


                   // findNavController().navigate(R.id.action_loginMobileFragment_to_otpMobileFragment,bundle)
                }
                else{
                    authViewModel.mobError.value="Please enter a valid mobile number."
                }


            }
        }

        }
    fun setKeyPad(PhonePad: RecyclerView) {
        authViewModel.mobError.value=""
        keyPad.clear()
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
        PhonePad.apply {
            //authViewModel.keyPadValue.value=getString(R.string.mobile_no_hint)
            adapter= PhonePadAdapter(keyPad,object : KeyPadOnClickListner {
                override fun onClick(item: Int) {
                    authViewModel.mobError.value = ""
                    authViewModel.keyPadValue.value?.apply {
                        if (item <= 9) {
                            if (this.length != 10) {
                                authViewModel.keyPadValue.value = "${this}$item"
                            }
                        } else if (item == 10) {
                            //authViewModel.keyPadValue.value =""
                        } else {
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
    fun generateRandomNumberInRange(): Int {
        return Random.nextInt(1000, 9999)
    }
    }


