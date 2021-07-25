package com.engx1.thegympodtvapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.model.InstructorProgramme

class SubProgrammeAdapter (val clickListener: (InstructorProgramme) -> Unit): RecyclerView.Adapter<SubProgrammeAdapter.ViewHolder>() {
    private val programmeList: MutableList<InstructorProgramme> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_subclass_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(programmeList[position].images?.listingImage).into(holder.programmeBanner)
        holder.subClassNameTV.text = programmeList[position].name
        holder.difficultyLevelTV.text = programmeList[position].level
        holder.intensityTV.text = programmeList[position].intensity.toString()
        ("${programmeList[position].totalMin} Minutes").also { holder.durationTV.text = it }
        holder.academyLayout.setOnClickListener {
            clickListener(programmeList[position])
        }
    }

    fun updateAdapter(list: List<InstructorProgramme>) {
        programmeList.clear()
        programmeList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        programmeList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return programmeList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val programmeBanner: ImageView = itemView.findViewById(R.id.programmeBannerIV)
        val academyLayout: CardView = itemView.findViewById(R.id.academyLayout)
        val subClassNameTV: TextView = itemView.findViewById(R.id.subClassNameTV)
        val difficultyLevelTV: TextView = itemView.findViewById(R.id.difficultyLevelTV)
        val intensityTV: TextView = itemView.findViewById(R.id.intensityTV)
        val durationTV: TextView = itemView.findViewById(R.id.durationTV)
    }
}