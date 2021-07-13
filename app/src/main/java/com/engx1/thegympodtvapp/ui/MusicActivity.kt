package com.engx1.thegympodtvapp.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beraldo.playerlib.PlayerService
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.api.legacy.ApiCallBack
import com.engx1.thegympodtvapp.api.legacy.ApiManager
import com.engx1.thegympodtvapp.api.legacy.ApiResponseListener
import com.engx1.thegympodtvapp.databinding.ActivityMusicBinding
import com.engx1.thegympodtvapp.model.AvailableMusicResponse
import com.engx1.thegympodtvapp.model.MusicResponse
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.ProgressDialogUtils
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

            isRunning = true
            binding.controlButton.setImageResource(R.drawable.ic_baseline_stop)
        }

        getAvailableMusic()
    }

    private fun getAvailableMusic() {
        ProgressDialogUtils.show(this, "Updating music list..")
        if (CommonUtils.isOnline(this)) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<AvailableMusicResponse> {
                override fun onApiSuccess(response: AvailableMusicResponse, apiName: String) {
                    ProgressDialogUtils.dismiss()
                    playList = response.data
                    playIndex = SharedPrefManager.getPreferenceInt(this@MusicActivity, "play_index") ?: 0
                    val title = SharedPrefManager.getPreferenceString(this@MusicActivity, "music_title")
                    val genre = SharedPrefManager.getPreferenceString(this@MusicActivity, "music_genre")

                    if (title != "") {
                        binding.musicTV.text = title
                        binding.genreTV.text = genre
                    } else {
                        binding.musicTV.text = playList[0].title
                        binding.genreTV.text = playList[0].genres
                        playIndex = 0
                    }
                }

                override fun onApiError(responses: String, apiName: String) {
                    ProgressDialogUtils.dismiss()
                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
                    ProgressDialogUtils.dismiss()
                }
            }, ApiService.GET_AVAILABLE_MUSIC, this.applicationContext)
            ApiManager(this).getAvailableMusic(apiCallBack)
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initMusic() {
        val m1 = MusicResponse("KPOP TOP 100", "KPOP", "http://121.159.140.57:8000")
        val m2 = MusicResponse("JPHiP Stream", "JPOP, KPOP, CPOP", "http://radio.jphip.com:8800")
        playList.add(m1)
        playList.add(m2)
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
        val musics = music.streamUrl
        val intent = Intent(this, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, musics)
        }
        SharedPrefManager.savePreferenceBoolean(this, "music_state", true)
        SharedPrefManager.savePreferenceInt(this, "play_index", playIndex)
        SharedPrefManager.savePreferenceString(this, "music_title", music.title)
        SharedPrefManager.savePreferenceString(this, "music_genre", music.genres)
        SharedPrefManager.savePreferenceString(this, "music_stream", music.streamUrl)
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