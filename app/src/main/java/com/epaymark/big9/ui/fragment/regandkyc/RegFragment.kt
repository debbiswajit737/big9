package com.epaymark.big9.ui.fragment.regandkyc



import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.window.layout.WindowMetricsCalculator
import com.epaymark.big9.R
import com.epaymark.big9.adapter.StateListAdapter
import com.epaymark.big9.data.model.StateCityModel
import com.epaymark.big9.data.model.sendData.onBoading.regForm
import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.databinding.FragmentRegBinding
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.fragment.CameraDialog
import com.epaymark.big9.utils.*
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.Constants.isBackCamera
import com.epaymark.big9.utils.helpers.PermissionUtils
import com.epaymark.big9.utils.helpers.PermissionUtils.createAlertDialog
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.`interface`.PermissionsCallback
import com.google.gson.Gson
import java.net.URLEncoder


class RegFragment : BaseFragment() {
    lateinit var binding: FragmentRegBinding
    var stateList = ArrayList<StateCityModel>()
    var cityList = ArrayList<StateCityModel>()
    var stateListAdapter:StateListAdapter?=null
    var cityListAdapter:StateListAdapter?=null
    var type=""
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg, container, false)
        binding.viewModel = authViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()

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
            spinnerGender.apply {
                val genderArray = arrayOf("Male", "Female")
                adapter = ArrayAdapter<String>(this.context, R.layout.custom_spinner_item, genderArray)
                setSpinner(object :CallBack{
                override fun getValue(s: String) {
                    authViewModel.genderReg.value=s
                   // Toast.makeText(binding.root.context, "$s", Toast.LENGTH_SHORT).show()
                }
            },genderArray)
            }
            tvDob.setOnClickListener {
                it.showDatePickerDialog(object :CallBack{
                    override fun getValue(s: String) {
                        //tvDob.text = s
                        viewModel?.dateOfBirth?.value=s
                        viewModel?.dateOfBirthErrorVisible?.value =
                        viewModel?.dateOfBirth?.value?.isNotEmpty() != true
                    }
                })
            }
            btnNext.setOnClickListener {

                if (authViewModel?.regValidation()==true) {



                    val regModel = regForm(
                        name = authViewModel.name.value,
                        mobile = authViewModel.mobile.value,
                        alternativeMobile = authViewModel.alternativeMobile.value,
                        email = authViewModel.email.value,
                        address = authViewModel.address.value,
                        pinCode = authViewModel.pinCode.value,
                        dateOfBirth = authViewModel.dateOfBirth.value,
                        state = authViewModel.state.value,
                        city = authViewModel.city.value,
                        area = authViewModel.area.value,
                        aadhar = authViewModel.aadhar.value,
                        panCardNo = authViewModel.panCardNo.value,
                        llPanBase64 = authViewModel.llPanBase64.value,
                        llCpanBase64 = authViewModel.llCpanBase64.value,
                        llBpanBase64 = authViewModel.llBpanBase64.value,
                        gender=authViewModel.genderReg.value
                    )

                    val gson = Gson()
                    val json = gson.toJson(regModel)
                    //json.toString().testDataFile()

                   findNavController().navigate(R.id.action_regFragment_to_kycDetailsFragment)
                }
            }

            activity?.let {act->
                llPan.setOnClickListener{
                    Constants.isBackCamera =true
                    type="llPan"
                    Constants.isPdf =false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {
                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llCpan.setOnClickListener{
                    llCpan.requestFocus()
                    hideKeyBoard(binding.etPan)
                    authViewModel.lastView=llCpan
                    isBackCamera =true
                    type="llCpan"
                    Constants.isPdf =false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llBpan.setOnClickListener{

                        llBpan.requestFocus()
                        hideKeyBoard(binding.etPan)


                    authViewModel.lastView=llBpan
                    isBackCamera =true
                    type="llBpan"
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

        /*binding.btnNext.setOnClickListener {
            //sendData()
            //browserPOST()

        }*/
        binding.tvCityListSearch.setOnClickListener {
            binding.tvCity.performClick()
        }
        binding.tvCity.setOnClickListener {
            //binding.etCity.setText("")
            authViewModel.city.value=""
            authViewModel.cityErrorVisible.value =false
            if (binding.recycleCity.isVisible){
                binding.recycleCity.visibility = View.GONE
                binding.etCity.visibility = View.GONE
                binding.tvCity.visibility = View.GONE
            }
            else {
                binding.recycleCity.visibility = View.VISIBLE
                binding.etCity.visibility = View.VISIBLE
                binding.tvCity.visibility = View.VISIBLE
            }
            binding.tvCityListSearch.isVisible = binding.recycleCity.isVisible
            it.isVisible=!binding.recycleCity.isVisible
        }





    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initView() {
        getScreenSize()
        setFocus()
        /*binding.llAadhar.setOnClickListener{
            myFocusCheck(binding.llAadhar)

        }
        binding.tvPancard3.setOnClickListener{
            myFocusCheck(binding.llAadhar)

        }
        binding.tvPancardImage1.setOnClickListener{
            myFocusCheck(binding.llAadhar)

        }*/

        checkPermission()
        // Add all the states
        stateList.add(StateCityModel(false,"Andhra Pradesh"))
        stateList.add(StateCityModel(false,"Arunachal Pradesh"))
        stateList.add(StateCityModel(false,"Assam"))
        stateList.add(StateCityModel(false,"Bihar"))
        stateList.add(StateCityModel(false,"Chhattisgarh"))
        stateList.add(StateCityModel(false,"Goa"))
        stateList.add(StateCityModel(false,"Gujarat"))
        stateList.add(StateCityModel(false,"Haryana"))
        stateList.add(StateCityModel(false,"Himachal Pradesh"))
        stateList.add(StateCityModel(false,"Jharkhand"))
        stateList.add(StateCityModel(false,"Karnataka"))
        stateList.add(StateCityModel(false,"Kerala"))
        stateList.add(StateCityModel(false,"Madhya Pradesh"))
        stateList.add(StateCityModel(false,"Maharashtra"))
        stateList.add(StateCityModel(false,"Manipur"))
        stateList.add(StateCityModel(false,"Meghalaya"))
        stateList.add(StateCityModel(false,"Mizoram"))
        stateList.add(StateCityModel(false,"Nagaland"))
        stateList.add(StateCityModel(false,"Odisha"))
        stateList.add(StateCityModel(false,"Punjab"))
        stateList.add(StateCityModel(false,"Rajasthan"))
        stateList.add(StateCityModel(false,"Sikkim"))
        stateList.add(StateCityModel(false,"Tamil Nadu"))
        stateList.add(StateCityModel(false,"Telangana"))
        stateList.add(StateCityModel(false,"Tripura"))
        stateList.add(StateCityModel(false,"Uttar Pradesh"))
        stateList.add(StateCityModel(false,"Uttarakhand"))
        stateList.add(StateCityModel(false,"West Bengal"))

// Add all the union territories
        stateList.add(StateCityModel(false,"Andaman and Nicobar Islands"))
        stateList.add(StateCityModel(false,"Chandigarh"))
        stateList.add(StateCityModel(false,"Dadra and Nagar Haveli and Daman and Diu"))
        stateList.add(StateCityModel(false,"Lakshadweep"))
        stateList.add(StateCityModel(false,"Delhi (National Capital Territory of Delhi)"))
        stateList.add(StateCityModel(false,"Puducherry"))
        stateList.add(StateCityModel(false,"Ladakh"))
        stateList.add(StateCityModel(false,"Jammu and Kashmir"))
        binding.recycleState.apply {
            binding.tvState.setOnClickListener {
                authViewModel.state.value="A"
                //authViewModel.stateErrorVisible.value=true
                //binding.etState.setText("")
                if (binding.recycleState.isVisible){
                    binding.recycleState.visibility = View.GONE
                    binding.etState.visibility = View.GONE
                }
                else {
                    binding.recycleState.visibility = View.VISIBLE
                    binding.etState.visibility = View.VISIBLE
                }

                binding.tvStateListSearch.isVisible=binding.recycleState.isVisible
                binding.tvState.isVisible=!binding.recycleState.isVisible
            }
            binding.tvStateListSearch.setOnClickListener {
                binding.recycleState.visibility=View.GONE
                binding.etState.visibility = View.GONE
                binding.tvState.isVisible=!binding.recycleState.isVisible
                binding.tvStateListSearch.isVisible=binding.recycleState.isVisible
            }
            stateListAdapter= StateListAdapter(stateList,object :CallBack{
                override fun getValue(s: String) {
                    //binding.tvState.text = s
                    authViewModel.state.value=s
                    authViewModel.stateErrorVisible.value=false
                    binding.recycleState.visibility=View.GONE
                    binding.etState.visibility = View.GONE
                    binding.tvState.isVisible=!binding.recycleState.isVisible
                    binding.tvStateListSearch.isVisible=binding.recycleState.isVisible
                }

            })
        adapter=stateListAdapter


        binding.etState.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    stateListAdapter?.filter?.filter(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }



        cityList.add(StateCityModel(false,"Kolkata"))
        cityList.add(StateCityModel(false,"Asansol"))
        cityList.add(StateCityModel(false,"Siliguri"))
        cityList.add(StateCityModel(false,"Kolkata"))
        cityList.add(StateCityModel(false,"Asansol"))
        cityList.add(StateCityModel(false,"Siliguri"))
        cityList.add(StateCityModel(false,"Kolkata"))
        cityList.add(StateCityModel(false,"Asansol"))
        cityList.add(StateCityModel(false,"Siliguri"))



        binding.recycleCity.apply {

            cityListAdapter= StateListAdapter(cityList,object :CallBack{
                override fun getValue(s: String) {


                    authViewModel.cityErrorVisible.value=false
                    binding.recycleCity.visibility=View.GONE
                    binding.etCity.visibility = View.GONE
                    binding.tvCity.visibility = View.GONE
                    binding.tvCityListSearch.visibility = View.GONE
                    binding.tvCity.isVisible=!binding.recycleCity.isVisible
                    //binding.tvCity.text = s
                    authViewModel.city.value=s
                }

            })
        adapter=cityListAdapter


        binding.etCity.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    cityListAdapter?.filter?.filter(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }


    }

    private fun getScreenSize() {
        activity?.let {act->
            val windowMetrics: androidx.window.layout.WindowMetrics =
                WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(act)
            val height = windowMetrics.bounds.height()
            val width = windowMetrics.bounds.width()
            setHeight(binding.recycleCity,height)
            setHeight(binding.recycleState,height)



        }

    }

    private fun setHeight(recycle: RecyclerView, height: Int) {
        val layoutParams = recycle.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = (height * 0.25).toInt()
        recycle.layoutParams = layoutParams
    }



    private fun setFocus() {
       binding.apply {
           /*etName.myFocusCheck()
           etMob.myFocusCheck()
           etAMob.myFocusCheck()*/
       }
    }

    private fun setObserver() {
        checkFocus()
        authViewModel.state.observe(viewLifecycleOwner){
            authViewModel.stateErrorVisible.value = it.isNotEmpty()
        }
        authViewModel.city.observe(viewLifecycleOwner){
            authViewModel.cityErrorVisible.value = it.isNotEmpty()
        }


        authViewModel?.filePath?.observe(viewLifecycleOwner){
            when(type){
                "llPan"->{
                    authViewModel.llPanBase64.value=it.uriToBase64(binding.root.context.contentResolver)
                    authViewModel.llPan.value=it.getFileNameFromUri()
                }
                "llCpan"->{
                    authViewModel.llCpanBase64.value=it.uriToBase64(binding.root.context.contentResolver)
                    authViewModel.llCpan.value=it.getFileNameFromUri()
                }
                "llBpan"->{
                    authViewModel.llBpanBase64.value=it.uriToBase64(binding.root.context.contentResolver)
                    authViewModel.llBpan.value=it.getFileNameFromUri()
                }
            }

            //Log.d("TAG_file", "true setObserver: "+it.uriToBase64(binding.root.context.contentResolver))
        }

    }

    private fun checkFocus() {

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission() {
        if (!PermissionUtils.hasVideoRecordingPermissions(binding.root.context)) {


            PermissionUtils.requestVideoRecordingPermission(binding.root.context, object : PermissionsCallback {
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
    fun dialogAllFileAccessPermissionAbove30() {
        createAlertDialog(
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

    private fun dialogRecordingPermission() {
        createAlertDialog(
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

    fun EditText.myFocusCheck(){
        this.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                this.setBackgroundResource(R.drawable.field_bg)
            } else {
                this.setBackgroundResource(R.drawable.field_bg_focus)
            }
        }
    }
    fun myFocusCheck(ll:LinearLayout){
        ll.setBackgroundResource(R.drawable.field_image_bg_focus)
    ll.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            ll.setBackgroundResource(R.drawable.field_image_bg_focus)
        } else {
            ll.setBackgroundResource(R.drawable.field_image_bg)
        }
    }
}
    fun sendData() {
        /*// Create a Uri with the URL you want to open
        val url = "https://www.gibl.in/wallet/validate2/?ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
        val uri = Uri.parse(url)

        // Create an Intent with ACTION_VIEW
        val intent = Intent(Intent.ACTION_VIEW)

        // Set the Uri with POST method and data
        intent.data = uri
        intent.putExtra("method", "POST")  // Specify the HTTP method as POST
        //intent.putExtra("ret_data", "eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0=")  // Add POST data
          // Add more POST data

        // Start the activity to open the web browser
        startActivity(intent)*/





        // Create a Uri with the URL you want to open
        val url = "https://www.gibl.in/wallet/validate2/"
        val uri = Uri.parse(url)

        // Create an Intent with ACTION_VIEW
        val intent = Intent(Intent.ACTION_VIEW)

        // Set the Uri with POST method and data
        intent.data = uri
        intent.putExtra("method", "POST")  // Specify the HTTP method as POST

        // Set the Content-Type header
        intent.putExtra("Content-Type", "text/html; charset=UTF-8")

        // Add other POST data if needed
        intent.putExtra("ret_data", "eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0=")

        // Start the activity to open the web browser
        startActivity(intent)
    }


    private fun browserPOST() {
        val i = Intent()
        // MUST instantiate android browser, otherwise it won't work (it won't find an activity to satisfy intent)
        i.component = ComponentName("com.android.browser", "com.android.browser.BrowserActivity")
        i.action = Intent.ACTION_VIEW
        val html: String = readTrimRawTextFile()

        // Replace params (if any replacement needed)

        // May work without url encoding, but I think is advisable
        // URLEncoder.encode replace space with "+", must replace again with %20
        val dataUri = "data:text/html," + URLEncoder.encode(html).replace("\\+".toRegex(), "%20")
        i.data = Uri.parse(dataUri)
        startActivity(i)
    }

    private fun readTrimRawTextFile(): String {
        var url="https://www.gibl.in/wallet/validate2/"
        var value="eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
        var html="<html>\n" +
                "        <body onLoad=\"document.getElementById('form').submit()\">\n" +
                "        <form id=\"form\" target=\"_self\" method=\"POST\" action=\"${url}\">\n" +
                "        <input type=\"hidden\" name=\"ret_data\" value=\"${value}\" />\n" +
                "        ...\n" +
                "        </form>\n" +
                "        </body>\n" +
                "        </html>"
        return html
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
                Constants.isVideo =false
                Constants.isGallary =false
                findNavController().navigate(R.id.action_regFragment_to_cameraFragment)
            }

        }
    }


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            authViewModel.filePath.value = uri

            //findNavController().navigate(R.id.action_homeFragment_to_previewFragment)
        } else {
            authViewModel.filePath.value =Uri.parse("/")

            Log.d("PhotoPicker", "No media selected")
        }

    }
}