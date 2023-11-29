package com.epaymark.big9.ui.fragment.regandkyc


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R
import com.epaymark.big9.data.model.sendData.onBoading.KycDetails

import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.databinding.KycDetailsFragmentBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.`interface`.CallBack
import com.google.gson.Gson

class KycDetailsFragment : BaseFragment() {
    lateinit var binding: KycDetailsFragmentBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.kyc_details_fragment, container, false)
        binding.viewModel = authViewModel
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
        binding.btnSaveContinue.setOnClickListener {
            if (authViewModel.kycValidation()) {
                authViewModel.apply {
                    val regModel = KycDetails(
                        businessType=businessType.value,
                        businessCategory=businessCategory.value,
                        partnerNameName=partnerNameName.value,
                        businessName=businessName.value,
                        businessAddress=businessAddress.value,
                        companyPanCardNumber=companyPanCardNumber.value,
                        kycAadharNumber=kycAadharNumber.value,
                        kycGSTNumber=kycGSTNumber.value,
                        partnerPanCardNumber=partnerPanCardNumber.value
                    )

                    val gson = Gson()
                    val json = gson.toJson(regModel)
                    json.toString().testDataFile()
                    findNavController().navigate(R.id.action_kycDetailsFragment_to_bankDetailsFragment)
                }

            }
        }

    }

    fun initView() {
        binding.apply {
            spinnerBusinessType.apply {
                val busnessType = arrayOf(
                    "Select Business Type",
                            "Limited",
                            "LLP",
                            "Others",
                            "Partnership",
                            "Private Limited",
                            "Proprietor")
                adapter = ArrayAdapter<String>(this.context, R.layout.custom_spinner_item, busnessType)
                setSpinner(object : CallBack {
                    override fun getValue(s: String) {
                        authViewModel.businessType.value=s
                        // Toast.makeText(binding.root.context, "$s", Toast.LENGTH_SHORT).show()
                    }
                },busnessType)
            }


            spinnerBusinessCategory.apply {
                val busnesscategory = arrayOf(
                    "Select Business Category",
                            "Agri Products Dealer",
                            "Auto Driver",
                            "Bakery/Confectionery",
                            "Bank Agent/facilitator",
                            "Black Smith",
                            "Books/Music",
                            "Branded Fast Food Outlet",
                            "Bus Driver",
                            "Candy, Nut, Confectionery Stores",
                            "Car Driver")
                adapter = ArrayAdapter<String>(this.context, R.layout.custom_spinner_item, busnesscategory)
                setSpinner(object : CallBack {
                    override fun getValue(s: String) {
                        authViewModel.businessCategory.value=s
                        // Toast.makeText(binding.root.context, "$s", Toast.LENGTH_SHORT).show()
                    }
                },busnesscategory)
            }



        }
    }

    fun setObserver() {

    }


    fun Spinner.setSpinner(callBack: CallBack, genderArray: Array<String>){
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                callBack.getValue(genderArray[position])
                // val selectedItem = items[position]
                // Handle the selected item
                //Toast.makeText(this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }
    }
}