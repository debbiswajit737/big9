package com.epaymark.big9.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.R
import com.epaymark.big9.databinding.ActivitySplashBinding
import com.epaymark.big9.ui.base.BaseActivity
import com.epaymark.big9.ui.receipt.AgreementPageFragment
import com.epaymark.big9.utils.helpers.Constants
import com.epaymark.big9.utils.helpers.PermissionUtils

import com.epaymark.big9.utils.helpers.SharedPreff
import com.epaymark.big9.utils.`interface`.CallBack
import com.epaymark.big9.utils.`interface`.PermissionsCallback
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    @Inject
    lateinit var sharedPreff: SharedPreff
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding= DataBindingUtil.setContentView(this, R.layout.activity_splash)
        notiPermission()
        getFireBAseToken()
        init()

    }

    private fun getFireBAseToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Token retrieval was successful
                val token = task.result
                sharedPreff.setfirebase_token(token)
            } else {
                /*// Token retrieval failed
                Log.e(TAG, "Failed to retrieve FCM token: ${task.exception}")*/
            }
        }
    }

    private fun notiPermission() {
        askNotificationPermission()
    }
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, you can now start your service or perform other actions that require this permission
        } else {
            // Permission denied, handle this according to your app's logic
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun init() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!PermissionUtils.hasVideoRecordingPermissions(this)) {
                try {
                    val dialogFragment = AgreementPageFragment(object: CallBack {
                        override fun getValue(s: String) {
                            if (Objects.equals(s,"permission required")) {
                                checkPermission()
                            }
                            else{
                                navigation()
                            }
                        }
                    })
                    dialogFragment.show(supportFragmentManager, dialogFragment.tag)
                }catch (e:Exception){

                }

            }
            else{
                navigation()
            }


        }, 3000)
        /*var a="abc".encryptData("ttt")

        var b="G0nJq3v8G2BEzY/5KRWbqppwDkw3e/YvN3b3KxY6hhqHZq0z4cxOt8QWAe+rOxzs8uEI5vgZetmbz4R6G2wP+vWbeZ9dOWFWNWaX+FAm5KFd2sdAoAmoYeX+7K5goOHaPkX6LizHGQWLienTnY6GYM2powYi6um2615Ejs/lzrspKwDeAm0xfSVZjhABcYA5"?.decryptData("a22786308b71488790be222216260e0a")
        Toast.makeText(this, "$b"+"\n"+a, Toast.LENGTH_SHORT).show()*/

        /*val key = "a22786308b71488790be222216260e0a"
        val iv = "656dbf654a5dc"

        val data = mapOf(
            "ClientID" to "big9_164604122023",
            "secretKey" to "677a05e769f1a888ddb86397eb45c57d2700bb7b83b4f3b7282bf6aba4266c7f",
            "mobile" to "9356561988"
        )
        val gson=Gson()
        val jsonData = gson.toJson(data)

// Encrypt
        val encryptedText = AesEncryptionUtil.encrypt(jsonData, key, iv)
        println("Encrypted Text: $encryptedText")

// Decrypt
        val decryptedText = AesEncryptionUtil.decrypt(encryptedText, key, iv)
        println("Decrypted Text: $decryptedText")*/

        val slideAnimation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.side_slide)
        binding.SplashScreenImage.startAnimation(slideAnimation)
    }

    private fun navigation() {
        val intent = if (sharedPreff?.checkIsLogin()==true){
            Intent(this, DashboardActivity::class.java)
        }
        else{
            Intent(this, RegActivity::class.java)
        }
        startActivity(intent)
        finish()
        //val intent = Intent(this, AuthActivity::class.java)
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
                val uri = Uri.fromParts("package", packageName, null)
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