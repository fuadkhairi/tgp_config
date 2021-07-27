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
import com.engx1.thegympodtvapp.databinding.FragmentClassVideoBinding
import com.engx1.thegympodtvapp.model.InstructorProgramme
import com.engx1.thegympodtvapp.utils.ProgressDialogUtils
import java.util.*
import java.util.concurrent.TimeUnit
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
        val instructorProgramme : InstructorProgramme? = arguments?.get("class") as InstructorProgramme?
        context?.let { ProgressDialogUtils.show(it, "Please wait...") }

        isIntroVideo = arguments?.getBoolean("isIntro") as Boolean
        if (isIntroVideo) {
            binding.classTitleTV.text = instructorProgramme?.name
        }


        sampleVideoView = binding.videoView
        sampleVideoView?.setVideoURI(Uri.parse(HLS_STREAMING_SAMPLE))

        playPauseButton = binding.playPauseButton
        playPauseButton?.setOnClickListener(this)

        seekBar = binding.seekBar
        seekBar?.setOnSeekBarChangeListener(this)

        runningTime = binding.runningTime
        runningTime?.text = "00:00"

        //Add the listeners
        sampleVideoView?.setOnCompletionListener(this)
        sampleVideoView?.setOnErrorListener(this)
        sampleVideoView?.setOnPreparedListener(this)
    }

    override fun onCompletion(p0: MediaPlayer?) {
        if (isIntroVideo!!) {
            activity?.onBackPressed()
        }
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