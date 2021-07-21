package com.engx1.thegympodtvapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.`interface`.AdapterClickListener
import com.engx1.thegympodtvapp.model.MainProgramme

class MainProgrammeAdapter (val clickListener: (MainProgramme) -> Unit): RecyclerView.Adapter<MainProgrammeAdapter.ViewHolder>() {
    private val programmeList: MutableList<MainProgramme> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_main_programme, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(programmeList[position].banner).into(holder.programmeBanner)
        holder.titleTV.text = programmeList[position].name
        holder.descriptionTV.text = programmeList[position].description

        holder.academyLayout.setOnClickListener {
            clickListener(programmeList[position])
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
        val academyLayout: RelativeLayout = itemView.findViewById(R.id.academyLayout)
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
    }
}