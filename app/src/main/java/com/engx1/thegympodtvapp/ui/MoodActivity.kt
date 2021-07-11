package com.engx1.thegympodtvapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.engx1.thegympodtvapp.`interface`.AdapterClickListener
import com.engx1.thegympodtvapp.adapter.ColorAdapter
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.databinding.ActivityMoodBinding
import com.engx1.thegympodtvapp.model.LightColor
import com.engx1.thegympodtvapp.model.MoodColorListResponse
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.viewmodel.MoodViewModel
import com.engx1.thegympodtvapp.viewmodel.MoodViewModelFactory
import retrofit2.Call

class MoodActivity : AppCompatActivity(), AdapterClickListener {
    private lateinit var binding: ActivityMoodBinding
    lateinit var viewModel: MoodViewModel

    private var moodColorList: ArrayList<LightColor> = ArrayList()
    private var colorAdapter: ColorAdapter? = null

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
        colorAdapter = ColorAdapter(this)

        binding.colorListRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.colorListRV.adapter = colorAdapter

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
            viewModel.getMoodColorList()
            viewModel.getDataMoodColorList().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            colorAdapter?.updateAdapter(it.data?.data?.colors!!)
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

    override fun onClicked(lightColor: LightColor) {

    }
}