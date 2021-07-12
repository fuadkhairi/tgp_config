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
import com.engx1.thegympodtvapp.model.MusicResponse
import com.engx1.thegympodtvapp.utils.SharedPrefManager

class MusicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicBinding
    private var isRunning = false
    private var playList: ArrayList<MusicResponse> = ArrayList()
    var playIndex = 0

    //NOTES
    //MUSIC PLAYER BUILT USING PLAYERLIB,GO TO
    //https://github.com/beraldofilippo/playerlib FOR INSTRUCTION HOW TO EMBED IN THE APP
    //July,11 2021 Fuad Khairi (@fuadkhairi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initMusic()

        binding.musicTV.text = playList[0].title
        binding.genreTV.text = playList[0].genres

        isRunning = isServiceRunningInForeground(this, PlayerService::class.java)
        if (isRunning) {
            binding.controlButton.setImageResource(R.drawable.ic_baseline_stop)
        } else {
            binding.controlButton.setImageResource(R.drawable.ic_baseline_play_arrow)
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.controlButton.setOnClickListener {
            if (isRunning) {
                isRunning = false
                stopPlayerService()
                binding.controlButton.setImageResource(R.drawable.ic_baseline_play_arrow)
            } else {
                isRunning = true
                startPlayerService(playList[playIndex])
                binding.controlButton.setImageResource(R.drawable.ic_baseline_stop)
            }
        }

        binding.skipNext.setOnClickListener {
            if (playIndex < playList.size-1) {
                playIndex++
                startPlayerService(playList[playIndex])
            } else {
                playIndex = 0
                startPlayerService(playList[playIndex])
            }
            Toast.makeText(this, playIndex.toString(), Toast.LENGTH_SHORT).show()
            isRunning = true
            binding.controlButton.setImageResource(R.drawable.ic_baseline_stop)
        }

        binding.skipPrev.setOnClickListener {
            if (playIndex > 0) {
                playIndex--
                startPlayerService(playList[playIndex])
            } else {
                playIndex = playList.size-1
                startPlayerService(playList[playIndex])
            }
            Toast.makeText(this, playIndex.toString(), Toast.LENGTH_SHORT).show()
            isRunning = true
            binding.controlButton.setImageResource(R.drawable.ic_baseline_stop)
        }
    }


    private fun initMusic() {
        val m1 = MusicResponse("KPOP TOP 100", "KPOP", "https://rush79.com", "http://121.159.140.57:8000")
        val m2 = MusicResponse("JPHiP Stream", "JPOP, KPOP, CPOP", "https://jphip.com", "http://radio.jphip.com:8800")
        val m3 = MusicResponse("HipHopRapture", "Hip Hop, Rap, East Coast Rap, West Coast Rap, Gangsta Rap", "https://hiphoprapture.com", "http://45.79.6.42:2410")
        playList.add(m1)
        playList.add(m2)
        playList.add(m3)
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

    private fun startPlayerService(music: MusicResponse) {
        (music.title).also { binding.musicTV.text = it }
        binding.genreTV.text = music.genres
        val music = music.streamUrl
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