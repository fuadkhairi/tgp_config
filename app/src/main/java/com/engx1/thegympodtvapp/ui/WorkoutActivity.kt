package com.engx1.thegympodtvapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.`interface`.AdapterClickListener
import com.engx1.thegympodtvapp.adapter.MainProgrammeAdapter
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.api.legacy.ApiCallBack
import com.engx1.thegympodtvapp.api.legacy.ApiManager
import com.engx1.thegympodtvapp.api.legacy.ApiResponseListener
import com.engx1.thegympodtvapp.databinding.ActivityWorkoutBinding
import com.engx1.thegympodtvapp.model.LightColor
import com.engx1.thegympodtvapp.model.MainWorkoutResponse
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen

class WorkoutActivity : AppCompatActivity(), AdapterClickListener {
    private lateinit var binding: ActivityWorkoutBinding
    private var programmeAdapter: MainProgrammeAdapter? = null
    private var skeletonScreen: SkeletonScreen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        programmeAdapter = MainProgrammeAdapter(this)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.mainProgrammeRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainProgrammeRV.adapter = programmeAdapter

        skeletonScreen = Skeleton.bind(binding.mainProgrammeRV).adapter(programmeAdapter).load(R.layout.layout_skeleton_programme).show()

        getMainProgramme()
    }

    private fun getMainProgramme() {
        if (CommonUtils.isOnline(this)) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<MainWorkoutResponse> {
                override fun onApiSuccess(response: MainWorkoutResponse, apiName: String) {
                    skeletonScreen?.hide()
                    programmeAdapter?.updateAdapter(response.data)
                    binding.mainProgrammeRV.scheduleLayoutAnimation()
                }

                override fun onApiError(responses: String, apiName: String) {
                    skeletonScreen?.hide()
                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
                    skeletonScreen?.hide()
                }
            }, ApiService.GET_MAIN_PROGRAMME, this.applicationContext)
            ApiManager(this).getMainProgramme(apiCallBack)
        } else {
            skeletonScreen?.hide()
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClicked(lightColor: LightColor, position: Int) {
    }
}