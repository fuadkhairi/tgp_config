package com.engx1.thegympodtvapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.databinding.ActivityMoodBinding

class MoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }
}