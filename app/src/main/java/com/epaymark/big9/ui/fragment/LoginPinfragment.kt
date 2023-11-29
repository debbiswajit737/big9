package com.epaymark.big9.ui.fragment


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.R

import com.epaymark.big9.adapter.PhonePadAdapter2
import com.epaymark.big9.data.model.ReceiptModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentLoginPinBinding

import com.epaymark.big9.ui.activity.RegActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.PermissionUtils
import com.epaymark.big9.utils.`interface`.KeyPadOnClickListner
import com.epaymark.big9.utils.`interface`.PermissionsCallback

class LoginPinfragment : BaseFragment() {
    lateinit var binding: FragmentLoginPinBinding
    var keyPad = ArrayList<Int>()
    private val myViewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_pin, container, false)
        binding.viewModel = myViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {
        binding.apply {
            tvSwitchAcc.setOnClickListener {
                activity?.let {act->
                    val intent = Intent(act, RegActivity::class.java)
                    intent.putExtra("isForgotPin",true)
                    startActivity(intent)
                    act.finish()
                }

            }
            tvForgotPassword.setOnClickListener{
                findNavController().navigate(R.id.action_loginPinfragment_to_forgotPasswordOtpFragment)
            }
        }


    }

    override fun onResume() {
        super.onResume()

    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initView() {
        checkPermission()
        setKeyPad(binding.recyclePhonePad)
        binding.apply {
            recyclePhonePad.requestFocus()
            hideKeyBoard(firstPinView)
        }

        //transition slip
        myViewModel.receiveStatus.value=""
        myViewModel.receiveReceptMessahe.value=getString(R.string.transaction_slip)
        Constants.recycleViewReceiptList.clear()

        Constants.recycleViewReceiptList.add(ReceiptModel(type = 4))
        Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "ACCOUNT NUMBER", reportValue ="300000025" ))
        Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "BANK NAME", reportValue ="AXIS BANK" ))
        Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "BENEFICIARY NAME", reportValue ="test value" ))
        Constants.recycleViewReceiptList.add(ReceiptModel(type = 1, property = "SENDER NUMBER", reportValue ="9234268887" ))
        Constants.recycleViewReceiptList.add(ReceiptModel(type = 2, title = "TRANSACTION DATE: 2023-09-09 14:44:26" ))

        Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = "300000085", rrnId = "325220891591", price = "100" , transactionMessage = "Refund", userName = "Test User"))

        Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = "300000085", rrnId = "325220891591", price = "100" , transactionMessage = "Refund", userName = "Test User"))

        Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = "300000085", rrnId = "325220891591", price = "100" , transactionMessage = "Refund", userName = "Test User"))

        Constants.recycleViewReceiptList.add(ReceiptModel(type = 3, transactionId = "300000085", rrnId = "325220891591", price = "100" , transactionMessage = "Refund", userName = "Test User"))



        //Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Id","300000025", type = 1))
        /*Constants.recycleViewReceiptList.add(ReceiptModel("Subscriber/ Customer Number","8583863153", type = 1))
        Constants.recycleViewReceiptList.add(ReceiptModel("Transaction Amount","₹10.00", type = 2))
        Constants.recycleViewReceiptList.add(ReceiptModel("Running Balance","₹200.22", type = 3))
        Constants.recycleViewReceiptList.add(ReceiptModel("Operator","AIRTEL", type = 4))
        Constants.recycleViewReceiptList.add(ReceiptModel("Operator ID","N/A", type = 1))*/

        /*val dialogFragment = ReceptDialogFragment()
        dialogFragment.show(childFragmentManager, dialogFragment.tag)*/

    }

    fun setObserver() {


    }

    fun setKeyPad(PhonePad: RecyclerView) {
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

            adapter= PhonePadAdapter2(keyPad,object : KeyPadOnClickListner {
                override fun onClick(item: Int) {
                    //myViewModel.loginPin.value?.let {
                        if (item<=9 ) {
                            if (myViewModel.loginPin.value?.length!=6) {
                                val loginPin="${myViewModel.loginPin.value}$item"
                                myViewModel.loginPin.value= loginPin
                                if(myViewModel.loginPin.value?.length==6){
                                    if (myViewModel.loginPin.value=="123456") {
                                        findNavController().navigate(R.id.action_loginPinfragment_to_homeFragment2)
                                    }
                                }

                            }
                        }

                        else {
                            if (myViewModel.loginPin.value?.isNotEmpty() == true) {
                                myViewModel.loginPin.value = this.toString().substring(0,
                                    myViewModel.loginPin.value?.length?.minus(1) ?: 0
                                )
                            }

                        }
                    //}
                }

            })
            isNestedScrollingEnabled=false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission() {
        if (!PermissionUtils.hasVideoRecordingPermissions(binding.root.context)) {


            PermissionUtils.requestVideoRecordingPermission(binding.root.context, object :
                PermissionsCallback {
                override fun onPermissionRequest(granted: Boolean) {
                    if (!granted) {
                        dialogRecordingPermission()

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (!Environment.isExternalStorageManager()) {
                                dialogAllFileAccessPermissionAbove30()
                            }

                        }

                    }

                }

            })

        }
    }

    private fun dialogRecordingPermission() {
        PermissionUtils.createAlertDialog(
            binding.root.context,
            "Permission Denied!",
            "Go to setting and enable recording permission",
            "OK", ""
        ) { value ->
            if (value) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    fun dialogAllFileAccessPermissionAbove30() {
        PermissionUtils.createAlertDialog(
            binding.root.context,
            "All file permissions",
            "Go to setting and enable all files permission",
            "OK", ""
        ) { value ->
            if (value) {
                val getpermission = Intent()
                getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getpermission)
            }
        }
    }
}