package com.epaymark.big9.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil

import com.epaymark.big9.utils.helpers.SharedPreff
import com.epaymark.epay.R
import com.epaymark.epay.databinding.ActivitySplashBinding
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

            val intent = if (sharedPreff.checkIsLogin()==true){
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
        val slideAnimation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.side_slide)
        binding.SplashScreenImage.startAnimation(slideAnimation)
    }

}