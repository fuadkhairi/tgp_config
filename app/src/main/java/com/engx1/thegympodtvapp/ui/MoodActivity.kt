package com.engx1.thegympodtvapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.engx1.thegympodtvapp.`interface`.AdapterClickListener
import com.engx1.thegympodtvapp.adapter.ColorAdapter
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.api.legacy.ApiCallBack
import com.engx1.thegympodtvapp.api.legacy.ApiManager
import com.engx1.thegympodtvapp.api.legacy.ApiResponseListener
import com.engx1.thegympodtvapp.databinding.ActivityMoodBinding
import com.engx1.thegympodtvapp.model.LightColor
import com.engx1.thegympodtvapp.model.MoodColorListResponse
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.MoodViewModel
import com.engx1.thegympodtvapp.viewmodel.MoodViewModelFactory

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

        val savedLightSwitchState = SharedPrefManager.getBooleanPreferences(this, "light_switch")
        val savedMoodSwitchState = SharedPrefManager.getBooleanPreferences(this, "mood_switch")
        val savedMoodColorIndex = SharedPrefManager.getPreferenceInt(this, "mood_color_index")

        if (savedLightSwitchState) {
            binding.lightSwitch.isChecked = true
        }

        if (savedMoodSwitchState) {
            binding.moodSwitch.isChecked = true
        }


        binding.lightSwitch.setOnCheckedChangeListener { _, isChecked ->
            //Toast.makeText(this, "light $isChecked", Toast.LENGTH_SHORT).show()
            changeLightState(isChecked, "26")
            SharedPrefManager.savePreferenceBoolean(this, "light_switch", isChecked)
        }

        binding.moodSwitch.setOnCheckedChangeListener { _, isChecked ->
            //Toast.makeText(this, "mood $isChecked", Toast.LENGTH_SHORT).show()
            changeMoodState(isChecked, "26")
            SharedPrefManager.savePreferenceBoolean(this, "mood_switch", isChecked)
        }

        binding.closeApp.setOnClickListener {
            cleanDefault()
        }

        getMoodColorList(savedMoodColorIndex)
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

    private fun getMoodColorList(savedMoodColorIndex: Int?) {
        if (CommonUtils.isOnline(this)) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<MoodColorListResponse> {
                override fun onApiSuccess(response: MoodColorListResponse, apiName: String) {
                    moodColorList = response.data.colors
                    colorAdapter?.updateAdapter(response.data.colors)
                    savedMoodColorIndex?.let { colorAdapter?.setSelectedItem(it) }
                }

                override fun onApiError(responses: String, apiName: String) {

                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
                }
            }, ApiService.GET_COLOR_LIST, this.applicationContext)
            ApiManager(this).getColorList(apiCallBack)
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCurrentMoodColor(color: String, gympodId: String) {
        if (CommonUtils.isOnline(this)) {
            viewModel.setMoodColor(color, gympodId)
            viewModel.getDataSetMoodColor().observe(this, {
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

    override fun onClicked(lightColor: LightColor, position: Int) {
        setCurrentMoodColor(lightColor.stripCode, "26")
        SharedPrefManager.savePreferenceInt(this, "mood_color_index", position)
    }

    private fun cleanDefault() {
        packageManager.clearPackagePreferredActivities(packageName)
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
        Toast.makeText(this, "Now you can close the app with home button", Toast.LENGTH_SHORT).show()
        finish()
    }
}