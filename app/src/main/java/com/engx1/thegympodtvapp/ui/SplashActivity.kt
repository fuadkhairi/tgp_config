package com.engx1.thegympodtvapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.ActivitySplashBinding
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.viewmodel.MainViewModel
import com.engx1.thegympodtvapp.viewmodel.MainViewModelFactory
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: MainViewModel

    private fun setupViewModel() {
        this.let {
            viewModel = ViewModelProvider(it, MainViewModelFactory(ApiClient.API_SERVICE)).get(MainViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timer().schedule(1500) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

    }

}