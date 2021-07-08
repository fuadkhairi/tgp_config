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
    //setContentView(R.layout.activity_main)

        binding.musicToggle.setOnClickListener {
            if (isServiceRunningInForeground(this, PlayerService::class.java)) {
                stopPlayerService()
                Toast.makeText(this, "Stopping service...", Toast.LENGTH_SHORT).show()
            } else {
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
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun stopPlayerService() {
        unbindService(connection)
        // Stop foreground service and remove the notification.
    }

    override fun onResume() {
        //pin the screen
        //startLockTask()

        //show realtime clock
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
        val currentDateTime = dateFormat.format(Date())
        binding.currentDateTime.text = currentDateTime

        super.onResume()
    }


    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {}

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is PlayerService.PlayerServiceBinder) {
                service.getPlayerHolderInstance() // use the player and call methods on it to start and stop
            }
        }
    }
}