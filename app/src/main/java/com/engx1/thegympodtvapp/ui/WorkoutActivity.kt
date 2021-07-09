package com.engx1.thegympodtvapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }
    }
}