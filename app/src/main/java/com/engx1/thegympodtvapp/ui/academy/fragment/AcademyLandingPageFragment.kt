package com.engx1.thegympodtvapp.ui.academy.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.FragmentAcademyLandingPageBinding
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory

class AcademyLandingPageFragment : Fragment() {

    private lateinit var binding: FragmentAcademyLandingPageBinding
    private lateinit var viewModel: AcademyViewModel


    private fun setupViewModel() {
        activity?.let {
            viewModel =
                ViewModelProvider(
                    it,
                    AcademyViewModelFactory(ApiClient.API_SERVICE)
                ).get(AcademyViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcademyLandingPageBinding.inflate(inflater, container, false)
        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { activity ->
            viewModel.getDataMainWorkout().observe(activity, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data.apply {
                                Log.d("WORKOUT", it.data.toString())
                            }
                        }
                        Resource.Status.ERROR -> {

                        }
                        Resource.Status.LOADING -> {

                        }
                    }
                }
            })
        }

        activity?.let { activity ->
            viewModel.getDataListInstructor().observe(activity, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data.apply {
                                Log.d("INSTRUCTOR", it.data.toString())
                            }
                        }
                        Resource.Status.ERROR -> {

                        }
                        Resource.Status.LOADING -> {

                        }
                    }
                }
            })
        }
    }

}