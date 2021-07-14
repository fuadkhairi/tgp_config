package com.engx1.thegympodtvapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.beraldo.playerlib.PlayerService
import com.engx1.thegympodtvapp.BuildConfig
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.api.legacy.ApiCallBack
import com.engx1.thegympodtvapp.api.legacy.ApiManager
import com.engx1.thegympodtvapp.api.legacy.ApiResponseListener
import com.engx1.thegympodtvapp.databinding.ActivityMainBinding
import com.engx1.thegympodtvapp.model.AvailableUpdateResponse
import com.engx1.thegympodtvapp.service.CountDownTimeService
import com.engx1.thegympodtvapp.utils.*
import com.engx1.thegympodtvapp.viewmodel.MainViewModel
import com.engx1.thegympodtvapp.viewmodel.MainViewModelFactory
import com.zubair.permissionmanager.PermissionManager
import com.zubair.permissionmanager.enums.PermissionEnum
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    var isBooked = false
    private val REQUEST_PERMISSIONS : Int = 101

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

        PermissionManager.Builder()
            .key(REQUEST_PERMISSIONS)
            .permission(PermissionEnum.READ_EXTERNAL_STORAGE, PermissionEnum.WRITE_EXTERNAL_STORAGE)
            .ask(this@MainActivity)

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
    }

    private val countDownUpdate: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.extras!!.getString("countdown") == "0") {
                binding.currentCountdown.visibility = View.INVISIBLE
                isBooked = false
            } else {
                ("Your session ends in " + intent.extras!!.getString("countdown")).also {
                    binding.currentCountdown.text = it
                }
            }
        }
    }

    private fun getAvailableUpdate() {
        if (CommonUtils.isOnline(this)) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<AvailableUpdateResponse> {
                override fun onApiSuccess(response: AvailableUpdateResponse, apiName: String) {
                    //compareVersion(response)
                }

                override fun onApiError(responses: String, apiName: String) {
                }

                override fun onApiFailure(failureMessage: String, apiName: String) {
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


    private fun compareVersion(compareTo: AvailableUpdateResponse) {
        val newVersion = compareTo.data[0].version
        val vc = VersionChecker()
        if (vc.getVersionStatus(newVersion) < 0) {
            showUpdateDialog(compareTo.data[0].downloadUrl)
        }
    }

    private fun showUpdateDialog(downloadUrl: String) {
        val layoutInflater = LayoutInflater.from(this)
        @SuppressLint("InflateParams") val popUp: View =
            layoutInflater.inflate(R.layout.layout_update_dialog, null)
        val alertDialogBuilder =
            AlertDialog.Builder(this)
        val updateBtn =
            popUp.findViewById<Button>(R.id.updateBT)
        alertDialogBuilder.setView(popUp)
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        updateBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Update started in the background..", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
            var destination: String =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/"
            val fileName = "AppName.apk"
            destination += fileName
            val uri = Uri.parse("file://$destination")
            val file = File(destination)
            if (file.exists())
                file.delete()
            val url: String = downloadUrl

            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            request.setDescription("downloading update...")
            request.setTitle("The Gympod Apps")
            request.setDestinationUri(uri)
            // get download service and enqueue file
            val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
            //set BroadcastReceiver to install app when .apk is downloaded
            val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context?, intent: Intent?) {
                    alertDialog.dismiss()
                    val toInstall = File(uri.path)
                    val apkUri: Uri = FileProvider.getUriForFile(
                        this@MainActivity,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        toInstall
                    )
                    val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
                    intent.data = apkUri
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    this@MainActivity.startActivity(intent)
                    unregisterReceiver(this)
                    finishAffinity()
                }
            }
            registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }
    }


    private fun getUserBookingProgramme() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getUserBookingProgramme()
            viewModel.getDataUserBookingProgramme().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data.apply {
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

    @SuppressLint("SimpleDateFormat")
    private fun getActiveBookings() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getActiveBooking(getCurrentTime())
            viewModel.getDataActiveBooking().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            if (it.data?.data?.data?.isNotEmpty()!!) {
                                isBooked = true
                                "Welcome, ${it.data.data?.data!![0].firstName}".also { s ->
                                    binding.currentDateTime.text = s
                                }
                                //val startTime = "2021-07-13 10:00:00"
                                //val endTime = "2021-07-13 10:15:00"
                                val endTime = it.data.data?.data!![0].endTime ?: ""
                                val startTime = it.data.data?.data!![0].startTime ?: ""
                                val currentTime = it.data.data?.currentTime ?: ""
                                val sdf = SimpleDateFormat("yyyy-dd-MM hh:mm:ss")
                                val c = sdf.parse(currentTime)
                                val e = sdf.parse(endTime)
                                val diff: Long = e.time - c.time
                                startService(
                                    Intent(
                                        this,
                                        CountDownTimeService::class.java
                                    ).putExtra("end_time", diff)
                                )
                                registerReceiver(countDownUpdate, IntentFilter("COUNTDOWN_UPDATED"))
                            } else {
                                //show realtime clock
                                val dateFormat =
                                    SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
                                val currentDateTime = dateFormat.format(Date())
                                binding.currentDateTime.text = currentDateTime
                            }
                        }
                        Resource.Status.ERROR -> {
                            val dateFormat =
                                SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
                            val currentDateTime = dateFormat.format(Date())
                            binding.currentDateTime.text = currentDateTime
                        }
                        Resource.Status.LOADING -> {
                            val dateFormat =
                                SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
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
        getAvailableUpdate()
        getMotivationalQuotes()
        getActiveBookings()
        getUserBookingProgramme()
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