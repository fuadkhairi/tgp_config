package com.engx1.thegympodtvapp.ui

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.engx1.thegympodtvapp.ui.academy.AcademyActivity
import com.engx1.thegympodtvapp.utils.*
import com.engx1.thegympodtvapp.viewmodel.MainViewModel
import com.engx1.thegympodtvapp.viewmodel.MainViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.zubair.permissionmanager.PermissionManager
import com.zubair.permissionmanager.enums.PermissionEnum
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    var isBooked = false
    var token = ""
    var name = ""
    var email = ""
    var gympodId = "gympod_1"
    var isActive = false
    //var enableFakeBook = true

    companion object {
        private val REQUEST_PERMISSIONS = 101
        private val REQUEST_CODE = 202
    }

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private fun setupViewModel() {
        this.let {
            viewModel = ViewModelProvider(
                it,
                MainViewModelFactory(ApiClient.API_SERVICE)
            ).get(MainViewModel::class.java)
        }
    }

    override fun onStart() {
        isActive = true
        super.onStart()
    }

    override fun onStop() {
        isActive = false
        super.onStop()
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
            //startActivity(Intent(this, AcademyActivity::class.java))
            val currentActiveName = SessionUtils.getSessionName(this)
            if (currentActiveName == name) {
                startActivity(Intent(this, AcademyActivity::class.java))
            } else {
                showLoginDialog()
            }
        }
        binding.moodToggle.setOnClickListener {
            startActivity(Intent(this, MoodActivity::class.java))
        }
        binding.musicToggle.setOnClickListener {
            startActivity(Intent(this, MusicActivity::class.java))
        }

        binding.logOutToggle.setOnClickListener {
            showEndSessionDialog()
        }
        //pin the screen
        //startLockTask()
        val musicIsRunning = SharedPrefManager.getBooleanPreferences(this, "music_state")
        if (musicIsRunning) {
            initializePlayService()
        }

        setupAlarmService()
    }

    private fun setupAlarmService() {
        val mIntent = Intent("HOME_UPDATE")
        val mPendingIntent =
            PendingIntent.getBroadcast(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mAlarmManager = this
            .getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //repeat every 5 minutes
        mAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
            300000, mPendingIntent
        )
        registerReceiver(updateDataReceiver, IntentFilter("HOME_UPDATE"))
    }

    private val updateDataReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            getAvailableUpdate()
            getActiveBookings()
            getMotivationalQuotes()
            Log.d("Update Home", "executed")
        }
    }

    private val countDownUpdate: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when {
                intent.extras!!.getString("countdown") == "0" -> {
                    binding.bookedView.visibility = View.GONE
                    isBooked = false
                    name = ""
                    email = ""
                    //enableFakeBook = false
                    SessionUtils.removeCurrentSession(this@MainActivity)
                    getActiveBookings()
                    if (isActive) {
                        showLogOutDialog()
                    }
                }
                else -> {
                    (intent.extras!!.getString("countdown")).also {
                        binding.currentCountdown.text = it
                    }
                }
            }
        }
    }

    private fun getAvailableUpdate() {
        if (CommonUtils.isOnline(this)) {
            val apiCallBack = ApiCallBack(object :
                ApiResponseListener<AvailableUpdateResponse> {
                override fun onApiSuccess(response: AvailableUpdateResponse, apiName: String) {
                    if (response.data.isNotEmpty()) {
                        compareVersion(response)
                    }
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
            if (compareTo.data[0].updateAllGympod) {
                showUpdateDialog(compareTo.data[0].downloadUrl)
            } else {
                for (id in compareTo.data[0].updateById) {
                    if (id == gympodId) {
                        showUpdateDialog(compareTo.data[0].downloadUrl)
                    }
                }
            }
        }
    }

    private fun showLoginDialog() {
        val layoutInflater = LayoutInflater.from(this)
        val popUp: View =
            layoutInflater.inflate(R.layout.layout_login, null)
        val alertDialogBuilder =
            AlertDialog.Builder(this)
        val welcomeTV =
            popUp.findViewById<TextView>(R.id.welcomeMessageTV)
        val emailET =
            popUp.findViewById<TextInputEditText>(R.id.emailET)
        val passwordET =
            popUp.findViewById<TextInputEditText>(R.id.passwordET)
        val loginBT =
            popUp.findViewById<Button>(R.id.loginBT)
        if (name != "") {
            welcomeTV.text = "Hello, $name"
        } else {
            welcomeTV.visibility = View.GONE
        }
        if (email != "") {
            emailET.setText(email)
        }
        alertDialogBuilder.setView(popUp)
        alertDialogBuilder.setCancelable(true)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        loginBT.setOnClickListener {
            if (emailET.text.toString() != "" || passwordET.text.toString() != "") {
                if (email != "") {
                    if (emailET.text.toString() == email) {
                        userLogin(emailET.text.toString(), passwordET.text.toString(), alertDialog)
                        ProgressDialogUtils.show(this)
                    } else {
                        Toast.makeText(this, "Only the active booking account can be logged in this session", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    userLogin(emailET.text.toString(), passwordET.text.toString(), alertDialog)
                    ProgressDialogUtils.show(this)
                }
            } else {
                Toast.makeText(this, "Complete the login data first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLogOutDialog() {
        val layoutInflater = LayoutInflater.from(this)
        val popUp: View =
            layoutInflater.inflate(R.layout.layout_session_end, null)
        val alertDialogBuilder =
            AlertDialog.Builder(this)
        val welcomeTV =
            popUp.findViewById<TextView>(R.id.welcomeMessageTV)
        val logOutBT =
            popUp.findViewById<Button>(R.id.logOutBT)
        welcomeTV.visibility = View.GONE

        alertDialogBuilder.setView(popUp)
        alertDialogBuilder.setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        logOutBT.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun showEndSessionDialog() {
        val layoutInflater = LayoutInflater.from(this)
        val popUp: View =
            layoutInflater.inflate(R.layout.layout_log_out, null)
        val alertDialogBuilder =
            AlertDialog.Builder(this)
        val endSessionBT =
            popUp.findViewById<Button>(R.id.endSessionBT)
        val cancelBT =
            popUp.findViewById<Button>(R.id.cancelBT)
        alertDialogBuilder.setView(popUp)
        alertDialogBuilder.setCancelable(true)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        endSessionBT.setOnClickListener {
            SessionUtils.removeCurrentSession(this)
            getActiveBookings()
            alertDialog.dismiss()
        }

        cancelBT.setOnClickListener {
            alertDialog.dismiss()
        }
    }


    private fun userLogin(email: String, password: String, alertDialog: AlertDialog) {
        if (CommonUtils.isOnline(this)) {
            viewModel.login(email, password, "ptx", "mirror", BuildConfig.VERSION_NAME)
            viewModel.getDataUserLogin().observe(this, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            ProgressDialogUtils.dismiss()
                            it.data.apply {
                                SessionUtils.saveSession(
                                    this@MainActivity,
                                    name,
                                    it.data?.data?.token!!
                                )
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        AcademyActivity::class.java
                                    )
                                )
                                alertDialog.dismiss()
                            }
                        }
                        Resource.Status.ERROR -> {
                            ProgressDialogUtils.dismiss()
                            alertDialog.dismiss()
                        }
                        Resource.Status.LOADING -> {
                            ProgressDialogUtils.dismiss()
                            alertDialog.dismiss()
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(
                this@MainActivity,
                "Update started in the background..",
                Toast.LENGTH_LONG
            ).show()
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
                            if (SessionUtils.getIsActiveSessionAvailable(this)) {
                                binding.logOutToggle.visibility = View.VISIBLE
                            } else {
                                binding.logOutToggle.visibility = View.GONE
                            }

                            if (it.data?.data?.data?.isNotEmpty()!!) {
                                isBooked = true
                                "Welcome, ${it.data.data?.data!![0].firstName}".also { s ->
                                    binding.welcomeMessageTV.text = s
                                }
                                binding.welcomeMessageTV.setTextColor(
                                    resources.getColor(
                                        R.color.colorPrimary,
                                        theme
                                    )
                                )

                                name = it.data.data?.data!![0].firstName.toString()
                                email = it.data.data?.data!![0].email.toString()

                                //val startTime = "2021-07-13 10:00:00"
                                //val endTime = "2021-07-13 10:15:00"

                                binding.bookedView.visibility = View.VISIBLE

                                //getUserBookingProgramme()

                                val dateFormat =
                                    SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
                                val currentDateTime = dateFormat.format(Date())
                                binding.currentDateTime.text = currentDateTime

                                val isRunning =
                                    isServiceRunning(this, CountDownTimeService::class.java)

                                if (!isRunning) {
                                    val endTime = it.data.data?.data!![0].endTime ?: ""
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
                                }
                                registerReceiver(countDownUpdate, IntentFilter("COUNTDOWN_UPDATED"))
                            } else {
                                //binding.bookedView.visibility = View.GONE
                                name = ""
                                email = ""
                                //SessionUtils.removeCurrentSession(this)
                                showCurrentDateTime()
//                                if (enableFakeBook) {
//                                    fakeBook()
//                                } else {
//                                    binding.bookedView.visibility = View.GONE
//                                    SessionUtils.removeCurrentSession(this)
//                                    showCurrentDateTime()
//                                }
                                fakeBook()
                            }
                        }
                        Resource.Status.ERROR -> {
                            showCurrentDateTime()
                        }
                        Resource.Status.LOADING -> {
                            showCurrentDateTime()
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fakeBook() {
        isBooked = true
        name = "Mas Faud"
        email = "banana@banana.com"

        "Welcome, $name".also { s ->
            binding.welcomeMessageTV.text = s
        }
        binding.welcomeMessageTV.setTextColor(
            resources.getColor(
                R.color.colorPrimary,
                theme
            )
        )

        val startTimeDummy = "2021-07-29 08:00:00"
        val endTimeDummy = "2021-07-29 08:00:25"

        binding.bookedView.visibility = View.VISIBLE

        //getUserBookingProgramme()

        val dateFormat =
            SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
        val currentDateTime = dateFormat.format(Date())
        binding.currentDateTime.text = currentDateTime

        val isRunning =
            isServiceRunning(this, CountDownTimeService::class.java)

        if (!isRunning) {
            val endTime = endTimeDummy
            val currentTime = startTimeDummy
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
        } else {
            stopService(
                Intent(
                    this,
                    CountDownTimeService::class.java
                )
            )
            val endTime = endTimeDummy
            val currentTime = startTimeDummy
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
        }
        registerReceiver(countDownUpdate, IntentFilter("COUNTDOWN_UPDATED"))
    }

    private fun showCurrentDateTime() {
        //show realtime clock
        val dateFormat =
            SimpleDateFormat("EEEE, dd MMMM, HH:mm a", Locale.ENGLISH)
        val currentDateTime = dateFormat.format(Date())
        binding.welcomeMessageTV.text = currentDateTime
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                if (service.started) {
                    return true
                }
            }
        }
        return false
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
        getActiveBookings()
        getMotivationalQuotes()
        name = ""
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