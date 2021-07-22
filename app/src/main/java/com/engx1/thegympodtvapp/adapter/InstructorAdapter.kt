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
import com.engx1.thegympodtvapp.model.Instructor

class InstructorAdapter (private val isHorizontalList: Boolean, val clickListener: (Instructor) -> Unit): RecyclerView.Adapter<InstructorAdapter.ViewHolder>() {
    private val instructorList: MutableList<Instructor> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = if (isHorizontalList) {
            inflater.inflate(R.layout.layout_instructor_list, parent, false)
        } else {
            inflater.inflate(R.layout.layout_all_instructor_list, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(instructorList[position].photo).into(holder.instructorIV)
        holder.instructorName.text = instructorList[position].name
        holder.instructorInfo.text = instructorList[position].description

        holder.instructorLayout.setOnClickListener {
            clickListener(instructorList[position])
        }
    }

    fun updateAdapter(list: List<Instructor>) {
        instructorList.clear()
        instructorList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return instructorList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instructorIV: ImageView = itemView.findViewById(R.id.instructorIV)
        val instructorLayout: RelativeLayout = itemView.findViewById(R.id.instructorLayout)
        val instructorName: TextView = itemView.findViewById(R.id.instructorNameTV)
        val instructorInfo: TextView = itemView.findViewById(R.id.instructorInfoTV)
    }
}