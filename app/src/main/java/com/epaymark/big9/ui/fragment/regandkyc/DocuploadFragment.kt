package com.epaymark.big9.ui.fragment.regandkyc


import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R

import com.epaymark.big9.data.viewMovel.AuthViewModel
import com.epaymark.big9.databinding.FragmentDocuploadBinding

import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.big9.ui.fragment.CameraDialog
import com.epaymark.big9.utils.common.MethodClass
import com.epaymark.big9.utils.helpers.Constants.isBackCamera
import com.epaymark.big9.utils.helpers.Constants.isGallary
import com.epaymark.big9.utils.helpers.Constants.isPdf
import com.epaymark.big9.utils.helpers.Constants.isVideo
import com.epaymark.big9.utils.helpers.SharedPreff
import com.epaymark.big9.utils.`interface`.CallBack
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class DocuploadFragment : BaseFragment() {
    lateinit var binding: FragmentDocuploadBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private var loader: Dialog? = null
    private val VIDEO_CAPTURE = 101
    var textView: TextView?=null
    var type=""
    @Inject
    override lateinit var sharedPreff: SharedPreff
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docupload, container, false)
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

    override fun onResume() {
        super.onResume()
        if (isVideo){
            loader?.let {
                it.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    authViewModel.videokyc.value=authViewModel.videoFilePath.value?.getFileNameFromUri()
                    it.dismiss()
                    isVideo=false
                },15000)
            }
        }
    }
    private fun onViewClick() {
        activity?.let {act->
            binding.apply {
                llVideoKyc.setOnClickListener{
                    isGallary=false
                    isVideo=true
                    isBackCamera=false
                    /*val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

                    // Set the maximum video duration in seconds (30 minutes = 30 * 60 seconds)
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5000)

                    // Start the video capture activity
                    startActivityForResult(intent, VIDEO_CAPTURE)*/
                    findNavController().navigate(R.id.action_docuploadFragment_to_cameraFragment)
                }



                llPartnerPanId.setOnClickListener{
                    isBackCamera=true
                    type="pan"
                    isPdf=false
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llCpan.setOnClickListener{
                    isBackCamera=true
                    isPdf=false
                    type="cpan"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llPaadhar.setOnClickListener{
                    isBackCamera=true
                    isPdf=false
                    type="paadhar"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llPartnerAadharBack.setOnClickListener{
                    isBackCamera=true
                    isPdf=false
                    type="PartnerAadharBack"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llGst.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llGst"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llCertificateOfIncorporation.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llCertificateOfIncorporation"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llBoardResolution.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llBoardResolution"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }
                llTrade.setOnClickListener{
                    isBackCamera=true
                    isPdf=true
                    type="llTrade"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llUserSelfi.setOnClickListener{
                    isBackCamera=false
                    isPdf=false
                    type="llUserSelfi"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                llCselfi.setOnClickListener{
                    isBackCamera=false
                    isPdf=false
                    type="llCselfi"
                    val cameraDialog = CameraDialog(object : CallBack {
                        override fun getValue(s: String) {

                            getImage(s)
                        }

                    })
                    cameraDialog.show(act.supportFragmentManager, cameraDialog.tag)
                }

                btnNext.setOnClickListener {

                    try {
                            binding.tvPancardVideoKycImage.setText(authViewModel.filePath.toString())
                            authViewModel.videoFile.value?.file=
                                authViewModel.filePath.value?.toString()?.videoToBase64(binding.root.context)
                                    .toString()

                            authViewModel.videoFilePath.value?.let {uri->
                                //Log.d("TAG_videofilebbbbb", "onViewClick: "+uriToBase64(binding.tvPancardVideoKycImage.context,uri))
                            }
                        if (authViewModel.docValidation()){
                            sharedPreff?.setLoginData()
                        startActivity(Intent(requireActivity(),DashboardActivity::class.java))
                        //Toast.makeText(binding.root.context, "Ok", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e:Exception){
                        Toast.makeText(binding.root.context, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }


    // Function to convert a video file URI to Base64
    fun uriToBase64(context: Context, videoUri: Uri): String {
        val contentResolver: ContentResolver = context.contentResolver

        // Open an input stream from the content resolver
        val inputStream: InputStream? = contentResolver.openInputStream(videoUri)

        // Use a ByteArrayOutputStream to read the data from the input stream
        val buffer = ByteArray(8192) // You can adjust the buffer size as needed
        val outputStream = ByteArrayOutputStream()

        try {
            var bytesRead: Int
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        } finally {
            inputStream?.close()
        }

        // Convert the bytes to a Base64 encoded string
        val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

        return base64String
    }

    private fun getImage(s:String) {
        when(s){
            "g"->{
                isVideo=false
                isGallary=true
                if (!isPdf) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                else{
                    findNavController().navigate(R.id.action_docuploadFragment_to_cameraFragment)
                }
                //
            }
            "t"->{
                isVideo=false
                isGallary=false
                findNavController().navigate(R.id.action_docuploadFragment_to_cameraFragment)
            }

        }
    }

    fun initView() {
        binding.root.context?.let {
            sharedPreff = SharedPreff(context)
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
    }

    fun setObserver() {
        authViewModel.filePath.observe(viewLifecycleOwner){
            when(type){
                "pan"->{
                    authViewModel.panPath.value=it?.getFileNameFromUri()
                }
                "cpan"->{
                    authViewModel.cpanPath.value=it?.getFileNameFromUri()
                }
                "paadhar"->{
                    authViewModel.paadhar.value=it?.getFileNameFromUri()
                }
                "PartnerAadharBack"->{
                    authViewModel.PartnerAadharBack.value=it?.getFileNameFromUri()
                }
                "llGst"->{
                authViewModel.llGst.value=it?.getFileNameFromUri()
                }
                "llCertificateOfIncorporation"->{
                authViewModel.llCertificateOfIncorporation.value=it?.getFileNameFromUri()
                }
                "llBoardResolution"->{
                authViewModel.llBoardResolution.value=it?.getFileNameFromUri()
                }
                "llTrade"->{
                authViewModel.llTrade.value=it?.getFileNameFromUri()
                }
                "llUserSelfi"->{
                authViewModel.llUserSelfi.value=it?.getFileNameFromUri()
                }
                "llCselfi"->{
                authViewModel.llCselfi.value=it?.getFileNameFromUri()
                }


            }

            //Log.d("TAG_file", "true setObserver: "+it.uriToBase64(binding.root.context.contentResolver))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val videoUri = data?.data

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Video saved to:\n"
                        + videoUri, Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Video recording cancelled.",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Failed to record video",
                    Toast.LENGTH_LONG).show()
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