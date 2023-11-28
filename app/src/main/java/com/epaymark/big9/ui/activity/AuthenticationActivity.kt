package com.epaymark.big9.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import com.epaymark.big9.data.viewMovel.AuthViewModel

import com.epaymark.epay.R
import com.epaymark.epay.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private var navController: NavController? = null
    lateinit var binding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_authentication)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]



        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        var currentFragmentId = navController?.currentDestination?.id
        if (currentFragmentId==R.id.cameraFragment){
            binding.g1.visibility= View.GONE
        }
        else{
            binding.g1.visibility= View.VISIBLE
        }

    }
}