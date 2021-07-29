package com.engx1.thegympodtvapp.ui.academy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.ActivityAcademyBinding
import com.engx1.thegympodtvapp.ui.MainActivity
import com.engx1.thegympodtvapp.ui.academy.fragment.AcademyLandingPageFragment
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory

class AcademyActivity : AppCompatActivity() {
    lateinit var binding: ActivityAcademyBinding
    lateinit var viewModel: AcademyViewModel
    lateinit var token: String
    var isActive = false

    private fun setupViewModel() {
        this.let {
            viewModel = ViewModelProvider(
                it,
                AcademyViewModelFactory(ApiClient.API_SERVICE)
            ).get(AcademyViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcademyBinding.inflate(layoutInflater)
        setupViewModel()
        token = SharedPrefManager.getPreferenceString(this, "token").toString()
        setContentView(binding.root)
        getMainWorkoutProgramme()
        getInstructorList()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.academyLayout, AcademyLandingPageFragment())
            .commit()

        registerReceiver(countDownUpdate, IntentFilter("COUNTDOWN_UPDATED"))
    }

    private val countDownUpdate: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when {
                intent.extras!!.getString("countdown") == "0" -> {
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

    override fun onStart() {
        isActive = true
        super.onStart()
    }

    override fun onStop() {
        isActive = false
        super.onStop()
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
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    private fun getMainWorkoutProgramme() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getMainWorkout("Bearer $token")
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getInstructorList() {
        if (CommonUtils.isOnline(this)) {
            viewModel.getListInstructor("Bearer $token", 50)
        } else {
            Toast.makeText(this, "Not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }
}