package com.big9.app.ui.receipt


import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentAgreementDialogBinding
import com.big9.app.ui.base.BaseCenterSheetFragment
import com.big9.app.utils.helpers.PermissionUtils
import com.big9.app.utils.`interface`.CallBack


class AgreementPageFragment(val callBack: CallBack) : BaseCenterSheetFragment() {
    lateinit var binding: FragmentAgreementDialogBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agreement_dialog, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        onViewClick()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun onViewClick() {
        binding.tvAgree.setOnClickListener{
            if (PermissionUtils.hasVideoRecordingPermissions(binding.tvAgree.context)){
                callBack.getValue("permission required")
                dismiss()
            }
            else{
             callBack.getValue("permission not required")
             dismiss()
            }

        }
        }


    fun initView() {
        setCrdViewMinHeight()

        val data = """
        Dear User,
        
        Thank you for choosing Big9. To provide you with the best experience and enable all of our app's features, we require the following permissions:
        
        • Bluetooth: This permission allows us to connect to Bluetooth devices for [specific feature or functionality].
        • Camera: We need access to your camera to [explain why you need camera access].
        • Record Audio: This permission is necessary for [explain why you need audio recording].
        • Access Media Location: This permission enables us to [explain why you need access to media locations].
        • Read External Storage: Required for [explain why you need read access to external storage].
        • Write External Storage: Necessary for [explain why you need write access to external storage].
        • Access Fine Location: This permission is essential for [explain why you need precise location access].
        • Access Coarse Location: Required for [explain why you need approximate location access].
        • Call Phone: This permission allows us to [explain why you need access to make phone calls].
        
        Please be assured that we take your privacy and data security seriously. We only use these permissions to provide you with a seamless and secure experience within our app. Your data will never be shared with third parties without your consent.
        
        If you have any questions or concerns regarding these permissions, please feel free to contact us at [your contact information]. We appreciate your trust in us and hope you enjoy using [Your App Name].
        
        Sincerely,
        The Big9 Team
    """.trimIndent()

        binding.tvMsg.text = data
    }

    private fun setCrdViewMinHeight() {
    }


    override fun onResume() {
        super.onResume()
        dialog?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                // Handle back press event here
                // Return true to consume the back press and prevent the dialog from being dismissed
                false
            } else {
                // Return false to allow the default back press behavior (dismiss the dialog)
                false
            }
        }
    }

}