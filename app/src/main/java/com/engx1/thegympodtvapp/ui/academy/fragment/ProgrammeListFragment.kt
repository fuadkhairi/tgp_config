package com.engx1.thegympodtvapp.ui.academy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.adapter.SubProgrammeAdapter
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.FragmentProgrammeListBinding
import com.engx1.thegympodtvapp.utils.Resource
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory


class ProgrammeListFragment : Fragment() {
    private lateinit var binding: FragmentProgrammeListBinding
    private lateinit var viewModel: AcademyViewModel
    private var subProgrammeAdapter: SubProgrammeAdapter? = null

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
        binding = FragmentProgrammeListBinding.inflate(inflater, container, false)
        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = SharedPrefManager.getPreferenceString(context, "token").toString()
        val programmeId : Int = arguments?.get("id") as Int
        val name: String = arguments?.get("name") as String

        binding.programmeNameTV.text = name

        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }

        subProgrammeAdapter = SubProgrammeAdapter {
            //Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
            val args = Bundle()
            args.putSerializable("class", it)
            val fragment = ClassDetailFragment()
            fragment.arguments = args
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.academyLayout, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        subProgrammeAdapter!!.clearAdapter()

        binding.subProgrammeRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.subProgrammeRV.adapter = subProgrammeAdapter

        viewModel.getProgrammeList("Bearer $token", programmeId)
        activity?.let { activity ->
            viewModel.getDataProgrammeList().observe(activity, {
                it.let {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data?.data?.data?.let { programmeList -> subProgrammeAdapter!!.updateAdapter(programmeList) }
                        }
                        Resource.Status.ERROR -> {
                            subProgrammeAdapter!!.clearAdapter()
                        }
                        Resource.Status.LOADING -> {
                            subProgrammeAdapter!!.clearAdapter()
                        }
                    }
                }
            })
        }

    }

}