package com.engx1.thegympodtvapp.ui.academy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.adapter.InstructorAdapter
import com.engx1.thegympodtvapp.adapter.MainProgrammeAdapter
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.FragmentAcademyLandingPageBinding
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory

class AcademyLandingPageFragment : Fragment() {

    private lateinit var binding: FragmentAcademyLandingPageBinding
    private lateinit var viewModel: AcademyViewModel
    private var academyAdapter: MainProgrammeAdapter? = null
    private var instructorAdapter: InstructorAdapter? = null

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
    ): View {
        binding = FragmentAcademyLandingPageBinding.inflate(inflater, container, false)
        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.seeAllTV.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.academyLayout, AllInstructorFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        academyAdapter = MainProgrammeAdapter {
            Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
        }
        binding.academyRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.academyRV.adapter = academyAdapter

        instructorAdapter = InstructorAdapter(true) {
            //Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
            val args = Bundle()
            args.putInt("id", it.id)
            val fragment = IndividualInstructorFragment()
            fragment.arguments = args
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.academyLayout, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.instructorRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.instructorRV.adapter = instructorAdapter

        activity?.let { activity ->
            viewModel.getDataMainWorkout().observe(activity, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data.apply {
                                academyAdapter!!.updateAdapter(it.data!!.data)
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
                                instructorAdapter!!.updateAdapter(it.data!!.data)
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