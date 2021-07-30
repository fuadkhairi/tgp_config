package com.engx1.thegympodtvapp.ui.academy.fragment

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.adapter.ClassVideoAdapter
import com.engx1.thegympodtvapp.api.ApiClient
import com.engx1.thegympodtvapp.databinding.FragmentClassDetailBinding
import com.engx1.thegympodtvapp.model.InstructorProgramme
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModel
import com.engx1.thegympodtvapp.viewmodel.AcademyViewModelFactory

class ClassDetailFragment : Fragment() {
    private lateinit var binding: FragmentClassDetailBinding
    private lateinit var token: String
    private lateinit var viewModel: AcademyViewModel
    private var isSubscribing: Boolean? = false

    private var classVideoAdapter: ClassVideoAdapter? = null

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
        val instructorProgramme : InstructorProgramme? = arguments?.get("class") as InstructorProgramme?

        isSubscribing = arguments?.get("subscription_status") as Boolean?

        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }

        classVideoAdapter = ClassVideoAdapter {
            if(it.vimeoId!=null && isSubscribing!!) {
                val args = Bundle()
                args.putSerializable("class", it)
                args.putBoolean("isIntro", false)
                val fragment = ClassVideoFragment()
                fragment.arguments = args
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.academyLayout, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            } else {
                showNeedForSubscription()
            }
        }
        binding.classVideoRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.classVideoRV.adapter = classVideoAdapter

        binding.introVideoLayout.setOnClickListener {
            if (isSubscribing!!) {
                val args = Bundle()
                args.putSerializable("class", instructorProgramme)
                args.putBoolean("isIntro", true)
                val fragment = ClassVideoFragment()
                fragment.arguments = args
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.academyLayout, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            } else {
                showNeedForSubscription()
            }

        }

        instructorProgramme?.let { programme ->
            binding.programmeTitleTV.text = programme.name
            binding.programmeTitleTV2.text = programme.name
            Glide.with(requireContext()).load(programme.images?.listingImage).into(binding.programmeBannerIV)
            Glide.with(requireContext()).load(programme.images?.listingImage).into(binding.introVideoIV)
            "${programme.level} - ${programme.totalMin} Minutes".also { binding.programmeLevelTV.text = it }
            "Intensity: ${programme.intensity}".also { binding.programmeIntensityTV.text = it }
            binding.descriptionTV.text = Html.fromHtml(programme.descriptions).toString()
            programme.exercises?.let { classVideoAdapter!!.updateAdapter(it) }
        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun showNeedForSubscription() {
        val layoutInflater = LayoutInflater.from(activity)
        val popUp: View =
            layoutInflater.inflate(R.layout.layout_general_dialog, null)
        val alertDialogBuilder =
            activity?.let { AlertDialog.Builder(it) }
        val messageTV =
            popUp.findViewById<TextView>(R.id.messageTV)
        val okBT =
            popUp.findViewById<Button>(R.id.endSessionBT)

        messageTV.text = resources.getString(R.string.to_view_the_content_please_subscribe_to_the_plan_via_our_mobile_app_or_academy_website)

        alertDialogBuilder?.setView(popUp)
        alertDialogBuilder?.setCancelable(true)
        val alertDialog = alertDialogBuilder?.create()
        alertDialog?.show()

        okBT.setOnClickListener {
            alertDialog?.dismiss()
        }
    }

}