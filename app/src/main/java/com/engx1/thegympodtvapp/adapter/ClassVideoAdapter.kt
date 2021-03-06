package com.engx1.thegympodtvapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.model.InstructorProgramme
import com.engx1.thegympodtvapp.model.ProgrammeExercise

class ClassVideoAdapter (val clickListener: (ProgrammeExercise) -> Unit): RecyclerView.Adapter<ClassVideoAdapter.ViewHolder>() {
    private val programmeList: MutableList<ProgrammeExercise> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_class_video_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(programmeList[position].images?.listingImage).into(holder.classThumbnailIV)
        holder.classVideoTitleTV.text = programmeList[position].name
        ("${programmeList[position].totalMin} Minutes").also { holder.totalMinsTV.text = it }
        holder.classVideoLayout.setOnClickListener {
            clickListener(programmeList[position])
        }
    }

    fun updateAdapter(list: List<ProgrammeExercise>) {
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
        val classThumbnailIV: ImageView = itemView.findViewById(R.id.classThumbnailIV)
        val classVideoLayout: LinearLayout = itemView.findViewById(R.id.classVideoLayout)
        val classVideoTitleTV: TextView = itemView.findViewById(R.id.classVideoTitleTV)
        val totalMinsTV: TextView = itemView.findViewById(R.id.totalMinsTV)
    }
}