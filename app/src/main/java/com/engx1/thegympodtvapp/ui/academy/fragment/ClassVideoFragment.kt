package com.engx1.thegympodtvapp.ui.academy.fragment

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.api.legacy.ApiCallBack
import com.engx1.thegympodtvapp.api.legacy.ApiManager
import com.engx1.thegympodtvapp.api.legacy.ApiResponseListener
import com.engx1.thegympodtvapp.databinding.FragmentClassVideoBinding
import com.engx1.thegympodtvapp.model.InstructorProgramme
import com.engx1.thegympodtvapp.model.LoggingResponse
import com.engx1.thegympodtvapp.model.VimeoConfigResponse
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.ProgressDialogUtils
import com.google.gson.JsonObject
import org.json.JSONObject
import kotlin.concurrent.fixedRateTimer


class ClassVideoFragment : Fragment(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private lateinit var binding: FragmentClassVideoBinding

    private val HLS_STREAMING_SAMPLE = "https://30vod-adaptive.akamaized.net/exp=1627428279~acl=%2F1cfd2916-9746-4097-84cb-a746f44f7755%2F%2A~hmac=70864b6d19fa20ecf2a5cce23957e1f76b2083fd1178ffb2b15929b2247ed2da/1cfd2916-9746-4097-84cb-a746f44f7755/sep/video/1ca189ec,d658efae,eb058328,f620bf58,fabf2f3e/audio/715d9ec9,d98110c5/master.m3u8?query_string_ranges=1"
    private var sampleVideoView: VideoView? = null
    private var seekBar: SeekBar? = null
    private var playPauseButton: ImageView? = null
    private var runningTime: TextView? = null
    private var currentPosition: Int = 0
    private var isRunning = false

    private var isIntroVideo: Boolean = false
    private var isLogged = false
    private var instructorProgramme : InstructorProgramme? = null

    private var logIdentifier: Int? = null
    private var watchDuration: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentClassVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instructorProgramme = arguments?.get("class") as InstructorProgramme?
        context?.let { ProgressDialogUtils.show(it, "Please wait...") }

        isIntroVideo = arguments?.getBoolean("isIntro") as Boolean
        if (isIntroVideo) {
            binding.classTitleTV.text = instructorProgramme?.name
        }

        sampleVideoView = binding.videoView
        playPauseButton = binding.playPauseButton
        playPauseButton?.setOnClickListener(this)

        seekBar = binding.seekBar
        seekBar?.setOnSeekBarChangeListener(this)

        runningTime = binding.runningTime
        runningTime?.text = "00:00"

        retrieveVimeoConfig(instructorProgramme?.introductionVimeoId.toString())

        //Add the listeners
        sampleVideoView?.setOnCompletionListener(this)
        sampleVideoView?.setOnErrorListener(this)
        sampleVideoView?.setOnPreparedListener(this)
    }

    private fun retrieveVimeoConfig(vimeoId: String) {
        if (CommonUtils.isOnline(requireContext())) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<VimeoConfigResponse> {
                override fun onApiSuccess(response: VimeoConfigResponse, apiName: String) {
                    sampleVideoView?.setVideoURI(Uri.parse(response.request.files.hls.cdns.akfireInterconnectQuic.url))
                }

                override fun onApiError(responses: String, apiName: String) {
                    Toast.makeText(context, responses, Toast.LENGTH_SHORT).show()
                    ProgressDialogUtils.dismiss()
                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
                    Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
                    ProgressDialogUtils.dismiss()
                }
            }, ApiService.GET_VIMEO_VIDEO, requireContext())
            ApiManager(requireContext()).getVimeoVideo(apiCallBack, vimeoId)
        } else {
            Toast.makeText(requireContext(), "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startLoggingVideo(id: Int) {
        if (CommonUtils.isOnline(requireContext())) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<LoggingResponse> {
                override fun onApiSuccess(response: LoggingResponse, apiName: String) {
                    logIdentifier = response.data.identifier
                    watchDuration = response.data.watchDuration
                }

                override fun onApiError(responses: String, apiName: String) {
                    Toast.makeText(context, responses, Toast.LENGTH_SHORT).show()
                    ProgressDialogUtils.dismiss()
                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
                    Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
                    ProgressDialogUtils.dismiss()
                }
            }, ApiService.LOG_VIDEO, requireContext())
            val jsonObject = JsonObject()
            jsonObject.addProperty("classIdentifier", id)
            ApiManager(requireContext()).startLoggingVideo(apiCallBack, jsonObject)
        } else {
            Toast.makeText(requireContext(), "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun endLoggingVideo(id: Int) {
        if (CommonUtils.isOnline(requireContext())) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<LoggingResponse> {
                override fun onApiSuccess(response: LoggingResponse, apiName: String) {

                }

                override fun onApiError(responses: String, apiName: String) {
                    Toast.makeText(context, responses, Toast.LENGTH_SHORT).show()
                    ProgressDialogUtils.dismiss()
                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
                    Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
                    ProgressDialogUtils.dismiss()
                }
            }, ApiService.LOG_VIDEO, requireContext())
            val jsonObject = JsonObject()
            jsonObject.addProperty("identifier", id)
            jsonObject.addProperty("isWatched", true)
            ApiManager(requireContext()).endLoggingVideo(apiCallBack, jsonObject)
        } else {
            Toast.makeText(requireContext(), "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {
        if (isIntroVideo) {
            activity?.onBackPressed()
        }
        isLogged = false
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        Toast.makeText(context, "Failed to play video", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onPrepared(p0: MediaPlayer?) {
        ProgressDialogUtils.dismiss()
        seekBar?.max = sampleVideoView?.duration!!
        sampleVideoView?.start()
        isRunning = true
        isLogged = false
        instructorProgramme?.id?.let { startLoggingVideo(it) }

        fixedRateTimer(name = "timer",
            initialDelay = 0, period = 1000) {
            refreshSeek()
        }

        playPauseButton?.setImageResource(R.drawable.lb_ic_pause)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun refreshSeek() {
        seekBar?.progress = sampleVideoView?.currentPosition!!

        if (!sampleVideoView?.isPlaying!!) {
            return
        }

        val time = sampleVideoView?.currentPosition!! / 1000
        val minute = (time / 60)
        val second = (time % 60)

        if (watchDuration!=null) {
            if (second > watchDuration!! && !isLogged) {
                isLogged = true
                logIdentifier?.let { endLoggingVideo(it) }
            }
        }

        activity?.runOnUiThread {
            runningTime?.text = String.format("%02d:%02d", minute, second)
        }
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        sampleVideoView?.seekTo(seekBar?.progress!!)
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.playPauseButton) {
            if (!isRunning) {
                isRunning = true
                sampleVideoView?.resume()
                sampleVideoView?.seekTo(currentPosition)
                playPauseButton?.setImageResource(R.drawable.lb_ic_pause)
            } else {
                isRunning = false
                sampleVideoView?.pause()
                currentPosition = sampleVideoView?.currentPosition!!
                playPauseButton?.setImageResource(R.drawable.lb_ic_play)
            }
        }
    }

}