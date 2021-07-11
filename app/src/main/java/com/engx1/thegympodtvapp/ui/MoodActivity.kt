package com.engx1.thegympodtvapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.ActivityMoodBinding
import com.engx1.thegympodtvapp.model.LightColor
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.viewmodel.MainViewModel
import com.engx1.thegympodtvapp.viewmodel.MainViewModelFactory
import com.engx1.thegympodtvapp.viewmodel.MoodViewModel
import com.engx1.thegympodtvapp.viewmodel.MoodViewModelFactory

class MoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoodBinding
    lateinit var viewModel: MoodViewModel

    var moodColorList: ArrayList<LightColor> = ArrayList()

    private fun setupViewModel() {
        this.let {
            viewModel = ViewModelProvider(
                it,
                MoodViewModelFactory(ApiClient.API_SERVICE)
            ).get(MoodViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.lightSwitch.setOnCheckedChangeListener { _, isChecked ->
            //Toast.makeText(this, "light $isChecked", Toast.LENGTH_SHORT).show()
            changeLightState(isChecked, "26")
        }

        binding.moodSwitch.setOnCheckedChangeListener { _, isChecked ->
            //Toast.makeText(this, "mood $isChecked", Toast.LENGTH_SHORT).show()
            changeMoodState(isChecked, "26")
        }

        getMoodColorList()
    }

    private fun changeLightState(newState: Boolean, gympodId: String) {
        if (CommonUtils.isOnline(this)) {
            viewModel.setLightState(newState, gympodId)
            viewModel.getDataSetLightState().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                        }
                        Resource.Status.ERROR -> {
                        }
                        Resource.Status.LOADING -> {
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeMoodState(newState: Boolean, gympodId: String) {
        if (CommonUtils.isOnline(this)) {
            viewModel.setMoodState(newState, gympodId)
            viewModel.getDataSetMoodState().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                        }
                        Resource.Status.ERROR -> {
                        }
                        Resource.Status.LOADING -> {
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMoodColorList() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getDataMoodColorList()
            viewModel.getDataMoodColorList().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data.apply {
                               moodColorList = this?.data?.colors!!
                            }
                        }
                        Resource.Status.ERROR -> {
                        }
                        Resource.Status.LOADING -> {
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }
}