package com.epaymark.big9.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentEpotlyBinding
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.SuccessPopupFragment
import com.epaymark.big9.ui.receipt.EPotlyReceptDialogFragment
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.`interface`.CallBack4
import java.util.Objects

class EpotlyFragment : BaseFragment() {
    lateinit var binding: FragmentEpotlyBinding
    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_epotly, container, false)
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

            imgBack.back()




            btnSubmit.setOnClickListener{
                activity?.let {act->
                    if (viewModel?.epotlyValidation() == true){
                        val tpinBottomSheetDialog = TpinBottomSheetDialog(object : CallBack {
                            override fun getValue(s: String) {
                                if (s=="123456"){
                                    val successPopupFragment = SuccessPopupFragment(object :
                                        CallBack4 {
                                        override fun getValue4(
                                            s1: String,
                                            s2: String,
                                            s3: String,
                                            s4: String
                                        ) {


                                                    val dialogFragment = EPotlyReceptDialogFragment(object: CallBack {
                                                        override fun getValue(s: String) {
                                                            if (Objects.equals(s,"back")) {
                                                                findNavController().popBackStack()
                                                            }
                                                        }
                                                    })
                                                    dialogFragment.show(childFragmentManager, dialogFragment.tag)

                                        }

                                    })
                                    successPopupFragment.show(childFragmentManager, successPopupFragment.tag)


                                }
                            }
                        })
                        tpinBottomSheetDialog.show(act.supportFragmentManager, tpinBottomSheetDialog.tag)

                    }
                }

            }

        }



    }

    fun initView() {
        binding.apply {
            etAmt.setupAmount()
        }
    }

    fun setObserver() {

    }


}