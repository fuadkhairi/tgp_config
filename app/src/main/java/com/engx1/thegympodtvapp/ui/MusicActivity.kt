package com.engx1.thegympodtvapp.ui

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beraldo.playerlib.PlayerService
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.databinding.ActivityMusicBinding
import com.engx1.thegympodtvapp.utils.SharedPrefManager

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    var isRunning = false

    //NOTES
    //MUSIC PLAYER BUILT USING PLAYERLIB,GO TO
    //https://github.com/beraldofilippo/playerlib FOR INSTRUCTION HOW TO EMBED IN THE APP
    //July,11 2021 Fuad Khairi (@fuadkhairi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        isRunning = isServiceRunningInForeground(this, PlayerService::class.java)
        if (isRunning) {
            binding.musicTV.text = "Music ON"
        } else {
            binding.musicTV.text = "Music OFF"
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.musicBTToggle.setOnClickListener {
            if (isRunning) {
                isRunning = false
                stopPlayerService()
                binding.musicTV.text = getString(R.string.music_off)
            } else {
                isRunning = true
                startPlayerService()
                binding.musicTV.text = getString(R.string.music_on)
            }
        }
    }

    private fun isServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    private fun startPlayerService() {
        val music = "http://167.114.64.181:8325/;stream/1"
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, music)
        }
        SharedPrefManager.savePreferenceBoolean(this, "music_state", true)
        startService(intent)
    }

    private fun stopPlayerService() {
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, "")
        }
        SharedPrefManager.savePreferenceBoolean(this, "music_state", false)
        stopService(intent)
    }

//    private val connection = object : ServiceConnection {
//        override fun onServiceDisconnected(name: ComponentName?) {}
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            if (service is PlayerService.PlayerServiceBinder) {
//                service.getPlayerHolderInstance() // use the player and call methods on it to start and stop
//            }
//        }
//    }
}