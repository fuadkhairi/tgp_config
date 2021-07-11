package com.engx1.thegympodtvapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beraldo.playerlib.PlayerService
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.ActivityMainBinding
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.Resource
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

//        binding.closeApp.setOnClickListener {
//            packageManager.clearPackagePreferredActivities(packageName);
//            val intent = Intent()
//            intent.action = Intent.ACTION_MAIN
//            intent.addCategory(Intent.CATEGORY_HOME)
//            startActivity(intent)
//            finish()
//        }

        //pin the screen
        //startLockTask()
        //initializePlayService()

        getActiveBookings()
    }

    private fun initializePlayService() {
        val kpop = "http://167.114.64.181:8325/;stream/1"
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, kpop)
        }
        startService(intent)
    }

    private fun getActiveBookings() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getActiveBooking(getCurrentTime())
            viewModel.getDataActiveBooking().observe(this, {
                it.let {
                    when(it.status) {
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

    private fun getMotivationalQuotes() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getMotivationalQuotes()
            viewModel.getDataMotivationalQuotes().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            (it.data?.data?.text + "\n- ${it.data?.data?.author}").also { s ->
                                binding.quotesTV.text = s
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

    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return dateFormat.format(Date())
    }

    override fun onResume() {
        //show realtime clock
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
        val currentDateTime = dateFormat.format(Date())
        binding.currentDateTime.text = currentDateTime
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