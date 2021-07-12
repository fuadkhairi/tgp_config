package com.engx1.thegympodtvapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.`interface`.AdapterClickListener
import com.engx1.thegympodtvapp.model.MainProgramme

class MainProgrammeAdapter (private val clickListener: AdapterClickListener): RecyclerView.Adapter<MainProgrammeAdapter.ViewHolder>() {
    private val programmeList: MutableList<MainProgramme> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_main_programme, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(programmeList[position].banner).into(holder.programmeBanner)

        holder.programmeBanner.setOnClickListener {
        }
    }

    fun updateAdapter(list: List<MainProgramme>) {
        programmeList.clear()
        programmeList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return programmeList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val programmeBanner: ImageView = itemView.findViewById(R.id.programmeBannerIV)
    }



}