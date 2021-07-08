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
import com.engx1.thegympodtvapp.databinding.ActivityMusicBinding

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        isRunning = isServiceRunningInForeground(this, PlayerService::class.java)

        binding.musicBTToggle.setOnClickListener {
            if (isRunning) {
                isRunning = false
                stopPlayerService()
                Toast.makeText(this, "Stopping service...", Toast.LENGTH_SHORT).show()
            } else {
                isRunning = true
                startPlayerService()
                Toast.makeText(this, "Starting service...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isServiceRunningInForeground(context: Context, serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.getClassName()) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    private fun startPlayerService() {
        val bbcRadio = "http://stream.live.vc.bbcmedia.co.uk/bbc_6music?s=1625665107&e=1625679507&h=6c9c0387f11bb7ef86ae48d0ef322eb7"
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, bbcRadio)
        }
        //bindService(intent, connection, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun stopPlayerService() {
        //unbindService(connection)
        val bbcRadio = "http://stream.live.vc.bbcmedia.co.uk/bbc_6music?s=1625665107&e=1625679507&h=6c9c0387f11bb7ef86ae48d0ef322eb7"
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, bbcRadio)
        }
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