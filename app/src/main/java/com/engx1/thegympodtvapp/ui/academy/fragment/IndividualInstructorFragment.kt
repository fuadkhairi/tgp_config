package com.engx1.thegympodtvapp.ui.academy.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.adapter.SocialMediaAdapter
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.FragmentIndividualInstructorBinding
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory

class IndividualInstructorFragment : Fragment() {
    private lateinit var binding: FragmentIndividualInstructorBinding
    private lateinit var viewModel: AcademyViewModel
    private lateinit var token: String

    private var socialMediaAdapter: SocialMediaAdapter? = null

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
        binding = FragmentIndividualInstructorBinding.inflate(inflater, container, false)
        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = SharedPrefManager.getPreferenceString(context, "token").toString()
        val instructorId : Int = arguments?.get("id") as Int

        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }

        socialMediaAdapter = SocialMediaAdapter {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
        }

        binding.socialMediaRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.socialMediaRV.adapter = socialMediaAdapter

        viewModel.getInstructorDetail("Bearer $token", instructorId)
        activity?.let { activity ->
            viewModel.getDataInstructorDetail().observe(activity, {
                it.let {
                    binding.instructorNameTV.text = it.data?.data?.instructor?.name
                    Glide.with(activity).load(it.data?.data?.instructor?.photo).into(binding.instructorIV)
                    binding.instructorSpecializationTV.text = it.data?.data?.instructor?.specialization
                    binding.instructorInfoTV.text = it.data?.data?.instructor?.description
                    it?.data?.data?.socialMedia?.let { it1 -> socialMediaAdapter!!.updateAdapter(it1) }
                }
            })
        }
    }

//    private fun getInstructorDetail(id: Int) {
//        if (CommonUtils.isOnline(requireContext())) {
//            val apiCallBack = ApiCallBack(object :
//                ApiResponseListener<InstructorDetailResponse> {
//                override fun onApiSuccess(response: InstructorDetailResponse, apiName: String) {
//                    binding.instructorNameTV.text = response.data?.instructor?.name
//                    Glide.with(requireContext()).load(response.data?.instructor?.photo).into(binding.instructorIV)
//                    binding.instructorSpecializationTV.text = response.data?.instructor?.specialization
//                    binding.instructorInfoTV.text = response.data?.instructor?.description
//                }
//
//                override fun onApiError(responses: String, apiName: String) {
//
//                }
//
//                override fun onApiFailure(failureMessage: String, apiName: String) {
//                }
//            }, ApiService.GET_INSTRUCTOR, requireContext())
//            ApiManager(requireContext()).getIndividualInstructor(apiCallBack, id)
//        } else {
//            Toast.makeText(context, "Not connected to Internet", Toast.LENGTH_SHORT).show()
//        }
//    }

}