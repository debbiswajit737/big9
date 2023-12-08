package com.epaymark.big9.ui.fragment



import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epaymark.big9.R
import com.epaymark.big9.adapter.PhonePadAdapter2
import com.epaymark.big9.data.model.ReceiptModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentLoginPinBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.activity.RegActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.LoadingPopup
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.PermissionUtils
import com.epaymark.big9.utils.`interface`.KeyPadOnClickListner
import com.epaymark.big9.utils.`interface`.PermissionsCallback
import com.google.gson.Gson


class LoginPinfragment : BaseFragment() {
    lateinit var binding: FragmentLoginPinBinding
    var keyPad = ArrayList<Int>()
    var loadingPopup: LoadingPopup? = null
    private var loader: Dialog? = null
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

        onViewClick()
        observer()
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
        activity?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
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

        /*val (isLogin, loginResponse) =sharedPreff.getLoginData()
        loginResponse?.let {loginData->


            val data = mapOf(
              "userid" to loginData.userid
            )

            val gson= Gson()
            var jsonString = gson.toJson(data)
            Log.d("TAG_otp_jsonString", "initView:jsonString "+jsonString)
            Log.d("TAG_otp_jsonString", "initView:encript "+jsonString.encrypt())
            //if (isLogin) {
               // loginData.AuthToken?.let {
                    myViewModel?.profile(auth,jsonString.encrypt())

                //}
           // }

        }*/
        callProfile()
    }

    private fun callProfile() {
        /*val (isLogin, loginResponse) =sharedPreff.getLoginData()


        loginResponse?.let {
            val data = mapOf(

                "userid" to it.userid,

                )
            val gson= Gson()
            var jsonString = gson.toJson(data)
            Log.d("TAG_p", "callProfile:json "+jsonString)
            Log.d("TAG_p", "callProfile:e \n"+jsonString.encrypt())
            loginResponse?.AuthToken?.let {
                var auth="eyJ1c2VyX2lkIjoiIiwidGltZXN0YW1wIjoxNzAxOTM5MTk2LCJyYW5kb20iOiJhN2ZjZWRjMjM1NzkxOGJlZDdjNjY2OGJjYmVhYmNhMmU4NWNhYWIwODE2ODg5ZjdhODY5YTY3MzBmNWY3Y2MzIn0="
                var data="ZqHbVba0bT2zVD2pxkGEH9tsoSvGvl18BD8ZpvkXvtM="
                myViewModel?.profile(auth,data)
               // myViewModel?.profile(it,jsonString.encrypt())
            }
        }*/

            /*"referenceid" to loginData.,*/

            //val a="eyJ1c2VyX2lkIjoiIiwidGltZXN0YW1wIjoxNzAxOTM5MTk2LCJyYW5kb20iOiJhN2ZjZWRjMjM1NzkxOGJlZDdjNjY2OGJjYmVhYmNhMmU4NWNhYWIwODE2ODg5ZjdhODY5YTY3MzBmNWY3Y2MzIn0="
        val (isLogin, loginResponse) =sharedPreff.getLoginData()
        loginResponse?.let {loginData->


            val data = mapOf(
                "otp" to "123456",
                "userid" to loginData.userid,

                "deviceid" to MethodClass.deviceUid(binding.root.context),
                "ipaddress" to MethodClass.getLocalIPAddress(),
                "location" to "123",
                "referenceid" to "123",
                "Timestamp" to MethodClass.getCurrentTimestamp()
            )
            /*"referenceid" to loginData.,*/
            val gson= Gson()
            var jsonString = gson.toJson(data)


                loginData.AuthToken?.let {
                    myViewModel?.profile2(it,jsonString.encrypt())
                }


        }


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
                                        findNavController().navigate(com.epaymark.big9.R.id.action_loginPinfragment_to_homeFragment2)
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

    private fun observer() {
        myViewModel?.profile2Response?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    it.data?.data?.SelfieImageData?.let {
                        val decodedString: ByteArray = Base64.decode(it, Base64.DEFAULT)
                        val decodedByte =
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                        Glide.with(this)
                            .asBitmap() // Use asBitmap() instead of asGif()
                            .load(decodedByte)
                            .error(R.drawable.ic_success) // Set the default image resource

                            .into(binding.profileImage)
                    }
                    myViewModel.apply {
                        userProfileMobile.value=it.data?.data?.mobileNo
                        userProfileName.value=it.data?.data?.name
                    }


                  //  Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()

                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }

   /* private fun setObserver() {
        myViewModel?.profileResponse?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loadingPopup?.show()
                }

                is ResponseState.Success -> {
                      loadingPopup?.dismiss()
                    Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                    }

                is ResponseState.Error -> {
                    //   loadingPopup?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }*/
}