package com.engx1.thegympodtvapp.ui.academy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.ActivityAcademyBinding
import com.engx1.thegympodtvapp.ui.academy.fragment.AcademyLandingPageFragment
import com.engx1.thegympodtvapp.utils.CommonUtils
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory

class AcademyActivity : AppCompatActivity() {
    lateinit var binding: ActivityAcademyBinding
    lateinit var viewModel: AcademyViewModel
    lateinit var token: String

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