package com.engx1.thegympodtvapp.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.beraldo.playerlib.PlayerService
import com.engx1.thegympodtvapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val kpop = "http://167.114.64.181:8325/;stream/1"
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, kpop)
        }
        //bindService(intent, connection, Context.BIND_AUTO_CREATE)
        startService(intent)

        //pin the screen
        //startLockTask()
    }


    override fun onResume() {
        //show realtime clock
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
        val currentDateTime = dateFormat.format(Date())
        binding.currentDateTime.text = currentDateTime

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