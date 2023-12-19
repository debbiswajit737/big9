package com.epaymark.big9.ui.fragment.regandkyc


import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.epaymark.big9.R
import com.epaymark.big9.adapter.PhonePadAdapter
import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.databinding.FragmentOtpMobileBinding
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetrofitHelper.handleApiError
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.activity.RegActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.popup.LoadingPopup
import com.epaymark.big9.utils.*
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.common.MethodClass.getCurrentTimestamp
import com.epaymark.big9.utils.common.MethodClass.getLocalIPAddress
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.`interface`.KeyPadOnClickListner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import java.util.TimeZone


class OtpMobileFragment : BaseFragment() {
    lateinit var binding: FragmentOtpMobileBinding
    var keyPad = ArrayList<Int>()
    val MY_PERMISSIONS_REQUEST_LOCATION=1
    private  val PERMISSION_REQUEST_LOCATION = 100
    private val authViewModel: AuthViewModel by activityViewModels()
    var isForgotPinPage=false
    val jsonDataLocation=JsonObject()
    private var loader: Dialog? = null
    //private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var loadingPopup: LoadingPopup? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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
        init()
        setKeyPad(binding.recyclePhonePad2)
        onViewClick()
        observer()
        isForgotPinPage= arguments?.getBoolean("isForgotPin") == true
    }

    private fun observer() {
        authViewModel?.otpResponse?.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Loading -> {
                    loader?.show()
                }

                is ResponseState.Success -> {
                    loader?.dismiss()
                    Toast.makeText(requireContext(), ""+it.data?.Description, Toast.LENGTH_SHORT).show()
                    if (it.data?.step==null){
                        (activity as? RegActivity)?.let {
                            val intent=Intent(requireActivity(), DashboardActivity::class.java)

                            startActivity(intent)
                            it.finish()


                       // if(authViewModel.otp.value=="123456"){
                                    //binding.lottieTickAnim.visibility=View.VISIBLE
                                   /* binding.lottieConfettiAnim.visibility=View.VISIBLE
                                    if (!isForgotPinPage) {
                                        it.data?.step?.let {
                                            setdata2(it)
                                        }

                                    }
                                    else{
                                        activity?.let {act->
                                            val intent = Intent(act, DashboardActivity::class.java)
                                            startActivity(intent)
                                            act.finish()
                                        }*/
                                    }
                                    //findNavController().navigate(R.id.action_otpFragment_to_congratulationFragment)
                                    // Toast.makeText(requireContext(), "match", Toast.LENGTH_SHORT).show()
                              //  }
                    }
                }

                is ResponseState.Error -> {
                    loader?.dismiss()
                    handleApiError(it.isNetworkError, it.errorCode, it.errorMessage)
                }
            }
        }
    }

    fun init(){
        activity?.let {act->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(act)
            loader = MethodClass.custom_loader(act, getString(R.string.please_wait))
            checkPermission2(act)
        }

       //activity?.let {  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)}
       // userLocation()
    }

    private fun checkPermission2(act: FragmentActivity) {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                act,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_LOCATION
            )
        } else {
            // Permission already granted, start location updates
            startLocationUpdates()
        }
    }


    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {act->
                checkPermission2(act)
           }

        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Use the location object to retrieve the location data
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val geocoder = Geocoder(binding.root.context, Locale.getDefault())
                    val addresses: List<Address>? =
                        geocoder.getFromLocation(latitude, longitude, 1)

                    // Process the addresses if available
                    addresses?.let {
                        if (it.isNotEmpty()) {
                            val address: Address = it[0]
                           /* val subAdminArea = address.subAdminArea
                            val continentName = address.countryName
                            val city = address.locality
                            val state = address.adminArea
                            val postalCode = address.postalCode
                            val knownName = address.featureName
                            val subLocality = address.subLocality
                            val addressLine1 = address.getAddressLine(0)
                            val addressLine2 = address.getAddressLine(1)*/


                            address?.let {
                                jsonDataLocation.addProperty("subAdminArea",it.subAdminArea)
                                jsonDataLocation.addProperty("continentName",it.countryName)

                                jsonDataLocation.addProperty("city",it.locality)
                                jsonDataLocation.addProperty("state",it.adminArea)

                                jsonDataLocation.addProperty("postalCode",it.postalCode)
                                jsonDataLocation.addProperty("knownName",it.featureName)
                                jsonDataLocation.addProperty("latitude",it.latitude)
                                jsonDataLocation.addProperty("longitude",it.longitude)

                                jsonDataLocation.addProperty("subLocality",it.subLocality)
                                jsonDataLocation.addProperty("address1",it.getAddressLine(1))
                                jsonDataLocation.addProperty("address2",it.getAddressLine(2))
                            }




                            // Now you have the location data, you can use it as needed
                            // For example, update UI with the location details
                        }
                    }
                }
            }
    }

    private fun userLocation() {
        // Check for runtime permissions
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }

        } else {
            // Permission already granted
            // Proceed with getting location
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        activity?.let {act->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(act)


            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude



                        val accuracy = location.accuracy

                        jsonDataLocation.addProperty("locationAccuracyRadius",accuracy)

                        getFullLocation(latitude,longitude)

                        // Do something with the latitude and longitude

                    } else {
                        // Handle the case where the location is null
                    }
                }
                .addOnFailureListener { e ->
                    // Handle errors that may occur while retrieving the location
                    Log.e("Location", "Error getting location", e)
                }
        }


    }

    private fun getCurrency() {

    }



    fun getFullLocation(latitude: Double, longitude: Double) {
        val defaultLocale = Locale.getDefault()
        Currency.getInstance( Locale("en", "IN")).apply {
            jsonDataLocation.addProperty("currencyCode",currencyCode)
            jsonDataLocation.addProperty("currencySymbol",symbol)
        }
        TimeZone.getDefault()?.let {
            jsonDataLocation.addProperty("timezone",it.id)
        }
        /*
        timezone
        currencyCode
        currencySymbol
        subAdminArea
        locationAccuracyRadius
        continentName
        */

        val geocoder: Geocoder
        val addresses: List<Address>?
        geocoder = Geocoder(binding.root.context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


       /* val address: String =
            addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses!![0].locality
        val state: String = addresses!![0].adminArea
        val country: String = addresses!![0].countryName
        val postalCode: String = addresses!![0].postalCode
        val knownName: String = addresses!![0].featureName*/
        addresses?.get(0)?.let {

            jsonDataLocation.addProperty("subAdminArea",it.subAdminArea)
            jsonDataLocation.addProperty("continentName",it.countryName)

            jsonDataLocation.addProperty("city",it.locality)
            jsonDataLocation.addProperty("state",it.adminArea)

            jsonDataLocation.addProperty("postalCode",it.postalCode)
            jsonDataLocation.addProperty("knownName",it.featureName)
            jsonDataLocation.addProperty("latitude",it.latitude)
            jsonDataLocation.addProperty("longitude",it.longitude)

            jsonDataLocation.addProperty("subLocality",it.subLocality)
            jsonDataLocation.addProperty("address1",it.getAddressLine(1))
            jsonDataLocation.addProperty("address2",it.getAddressLine(2))
        }

    }

   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLocation()
                } else {
                    // Permission denied
                    // Handle accordingly (e.g., show a message)
                }
            }
        }
    }*/

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
                                authViewModel.otp.value = "${this}${item}"
                                val (isLogin, loginResponse) =sharedPreff.getLoginData()
                                loginResponse?.let {loginData->


                                    val data = mapOf(
                                        "otp" to authViewModel.otp.value,
                                        "userid" to loginData.userid,

                                        "deviceid" to MethodClass.deviceUid(binding.root.context),
                                        "ipaddress" to getLocalIPAddress(),
                                        "location" to jsonDataLocation.toString(),
                                        "referenceid" to "123",
                                         "Timestamp" to getCurrentTimestamp()
                                    )
                                    /*"referenceid" to loginData.,*/
                                    val gson= Gson()
                                    var jsonString = gson.toJson(data)
                                    if (this.length==5) {
                                        loginData.AuthToken?.let {
                                        authViewModel?.sendOtp(it,jsonString.encrypt())
                                        }
                                    }

                                }
                                // binding.firstPinView.text=this

                                /*if(authViewModel.otp.value=="123456"){
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
                                }*/

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

    private fun setdata2(step: Int) {
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
                            val intent=Intent(requireActivity(), DashboardActivity::class.java)
                            //val intent=Intent(requireActivity(), AuthenticationActivity::class.java)
                            intent.putExtra("stape",step.toString())
                            startActivity(intent)
                            it.finish()
                        }
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                startLocationUpdates()
            } else {
                // Permission denied, handle accordingly (e.g., show a message or request again)
            }
        }
    }
    }


