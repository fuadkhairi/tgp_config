package com.engx1.thegympodtvapp.ui.academy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.FragmentClassDetailBinding
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory

class ClassDetailFragment : Fragment() {
    private lateinit var binding: FragmentClassDetailBinding
    private lateinit var token: String
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
    ): View {
        // Inflate the layout for this fragment
        setupViewModel()
        binding = FragmentClassDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        token = SharedPrefManager.getPreferenceString(context, "token").toString()
        val instructorId : Int = arguments?.get("id") as Int
        super.onViewCreated(view, savedInstanceState)
    }

}