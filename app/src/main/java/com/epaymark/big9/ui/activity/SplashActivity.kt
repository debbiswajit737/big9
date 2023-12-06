package com.epaymark.big9.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.epaymark.big9.R
import com.epaymark.big9.databinding.ActivitySplashBinding

import com.epaymark.epay.utils.helpers.SharedPreff
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    @Inject
    lateinit var sharedPreff: SharedPreff
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding= DataBindingUtil.setContentView(this, R.layout.activity_splash)
        init()



        Handler(Looper.getMainLooper()).postDelayed({

            val intent = if (sharedPreff?.checkIsLogin()==true){
                Intent(this, DashboardActivity::class.java)
            }
            else{
                Intent(this, RegActivity::class.java)
            }
            startActivity(intent)
            finish()
            //val intent = Intent(this, AuthActivity::class.java)

        }, 3000)
    }


    private fun init() {
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

}