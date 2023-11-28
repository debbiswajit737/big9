package com.epaymark.big9.ui.fragment.regandkyc



import android.animation.Animator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.R
import com.epaymark.big9.adapter.PhonePadAdapter
import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.databinding.FragmentOtpMobileBinding
import com.epaymark.big9.ui.activity.AuthenticationActivity
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.activity.RegActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.*
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.`interface`.KeyPadOnClickListner
import kotlinx.coroutines.launch


class OtpMobileFragment : BaseFragment() {
    lateinit var binding: FragmentOtpMobileBinding
    var keyPad = ArrayList<Int>()
    private val authViewModel: AuthViewModel by activityViewModels()
    var isForgotPinPage=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp_mobile, container, false)
        binding.viewModel=authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setKeyPad(binding.recyclePhonePad2)
        onViewClick()
        isForgotPinPage= arguments?.getBoolean("isForgotPin") == true
    }

    private fun onViewClick() {


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
                    authViewModel.otp.value?.apply {
                        if (item<=9 ) {
                            if (this.length!=6) {

                                // binding.firstPinView.text=this
                                authViewModel.otp.value = "${this}${item}"
                                if(authViewModel.otp.value=="123456"){
                                    //binding.lottieTickAnim.visibility=View.VISIBLE
                                    binding.lottieConfettiAnim.visibility=View.VISIBLE
                                    if (!isForgotPinPage) {
                                        setdata2()
                                    }
                                    else{
                                        activity?.let {act->
                                            val intent = Intent(act, DashboardActivity::class.java)
                                            startActivity(intent)
                                            act.finish()
                                        }
                                    }
                                    //findNavController().navigate(R.id.action_otpFragment_to_congratulationFragment)
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

    private fun setdata2() {
        //binding.lottieTickAnim.setAnimationFromUrl(Constants.LOTTIE_TICK_LINK)
        //binding.lottieTickAnim.playAnimation()
        binding.lottieConfettiAnim.setAnimationFromUrl(Constants.LOTTIE_CONFETTIE_LINK)
        //        lottie_anim.setSpeed(0.7f);
        binding.lottieConfettiAnim.playAnimation()
        binding.lottieConfettiAnim.addAnimatorListener(
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    lifecycleScope.launch {
                        //delay(500L)
                        (activity as? RegActivity)?.let {
                            startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
                            it.finish()
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }
    }


