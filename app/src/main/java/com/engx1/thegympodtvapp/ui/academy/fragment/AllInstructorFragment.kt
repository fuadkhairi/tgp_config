package com.engx1.thegympodtvapp.ui.academy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.adapter.InstructorAdapter
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.FragmentAllInstructorBinding
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory


class AllInstructorFragment : Fragment() {
    private lateinit var binding: FragmentAllInstructorBinding
    private lateinit var viewModel: AcademyViewModel
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllInstructorBinding.inflate(inflater, container, false)
        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }

        instructorAdapter = InstructorAdapter(false) {
            Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
        }
        binding.allInstructorRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.allInstructorRV.adapter = instructorAdapter

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