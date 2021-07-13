package com.engx1.thegympodtvapp.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beraldo.playerlib.PlayerService
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.api.legacy.ApiCallBack
import com.engx1.thegympodtvapp.api.legacy.ApiManager
import com.engx1.thegympodtvapp.api.legacy.ApiResponseListener
import com.engx1.thegympodtvapp.databinding.ActivityMainBinding
import com.engx1.thegympodtvapp.model.AvailableUpdateResponse
import com.engx1.thegympodtvapp.service.CountDownTimeService
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.ProgressDialogUtils
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.MainViewModel
import com.engx1.thegympodtvapp.viewmodel.MainViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    private fun setupViewModel() {
        this.let {
            viewModel = ViewModelProvider(
                it,
                MainViewModelFactory(ApiClient.API_SERVICE)
            ).get(MainViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.workoutToggle.setOnClickListener {
            startActivity(Intent(this, WorkoutActivity::class.java))
        }
        binding.moodToggle.setOnClickListener {
            startActivity(Intent(this, MoodActivity::class.java))
        }
        binding.musicToggle.setOnClickListener {
            startActivity(Intent(this, MusicActivity::class.java))
        }
        //pin the screen
        //startLockTask()
        val musicIsRunning = SharedPrefManager.getBooleanPreferences(this, "music_state")
        if (musicIsRunning) {
            initializePlayService()
        }
        getActiveBookings()
        getAvailableUpdate()
    }

    private val countDownUpdate: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            if (intent.extras!!.getString("countdown") == "0") {
                binding.currentCountdown.visibility = View.INVISIBLE
            } else {
                ("Your session ends in " + intent.extras!!.getString("countdown")).also { binding.currentCountdown.text = it }
            }
        }
    }


    private fun getAvailableUpdate() {
        ProgressDialogUtils.show(this, "Getting updates..")
        if (CommonUtils.isOnline(this)) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<AvailableUpdateResponse> {
                override fun onApiSuccess(response: AvailableUpdateResponse, apiName: String) {
                    ProgressDialogUtils.dismiss()
                }

                override fun onApiError(responses: String, apiName: String) {
                    ProgressDialogUtils.dismiss()
                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
                    ProgressDialogUtils.dismiss()
                }
            }, ApiService.GET_AVAILABLE_UPDATE, this.applicationContext)
            ApiManager(this).getAvailableUpdate(apiCallBack)
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializePlayService() {
        val stream = SharedPrefManager.getPreferenceString(this, "music_stream")
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, stream)
        }
        startService(intent)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getActiveBookings() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getActiveBooking(getCurrentTime())
            viewModel.getDataActiveBooking().observe(this, {
                it.let {
                    when(it.status) {
                        Resource.Status.SUCCESS -> {
                            if (it.data?.data?.data?.isNotEmpty()!!) {
                                "Welcome, ${it.data.data?.data!![0].firstName}".also { s -> binding.currentDateTime.text = s }
                                //val startTime = "2021-07-13 10:00:00"
                                //val endTime = "2021-07-13 10:15:00"
                                val endTime = it.data.data?.data!![0].endTime ?: ""
                                val startTime = it.data.data?.data!![0].startTime ?: ""
                                val sdf = SimpleDateFormat("yyyy-dd-MM hh:mm:ss")
                                val s = sdf.parse(startTime)
                                val e = sdf.parse(endTime)
                                val diff: Long = e.time - s.time
                                startService(Intent(this, CountDownTimeService::class.java).putExtra("end_time", diff))
                                registerReceiver(countDownUpdate, IntentFilter("COUNTDOWN_UPDATED"))
                            } else {
                                //show realtime clock
                                val dateFormat = SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
                                val currentDateTime = dateFormat.format(Date())
                                binding.currentDateTime.text = currentDateTime
                            }
                        }
                        Resource.Status.ERROR -> {
                            val dateFormat = SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
                            val currentDateTime = dateFormat.format(Date())
                            binding.currentDateTime.text = currentDateTime
                        }
                        Resource.Status.LOADING -> {
                            val dateFormat = SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
                            val currentDateTime = dateFormat.format(Date())
                            binding.currentDateTime.text = currentDateTime
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMotivationalQuotes() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getMotivationalQuotes()
            viewModel.getDataMotivationalQuotes().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            binding.quotesTV.text = it.data?.data?.text
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

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return dateFormat.format(Date())
    }

    override fun onResume() {
        getMotivationalQuotes()
        super.onResume()
    }

    override fun onBackPressed() {
    }

    override fun onDestroy() {
        super.onDestroy()
        val bbcRadio = ""
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, bbcRadio)
        }
        stopService(intent)
    }
}