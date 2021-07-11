package com.engx1.thegympodtvapp.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.`interface`.AdapterClickListener
import com.engx1.thegympodtvapp.model.LightColor

class ColorAdapter(private val clickListener: AdapterClickListener): RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    private val colorList: MutableList<LightColor> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_color, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.colorItem.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorList[position].hexCode))

        holder.colorItem.setOnClickListener {
            selectedPos = position
            clickListener.onClicked(colorList[position])
            notifyDataSetChanged()
        }

        if (selectedPos == position) {
            holder.colorSelectedItem.visibility = View.VISIBLE
        } else {
            holder.colorSelectedItem.visibility = View.INVISIBLE
        }
    }

    fun updateAdapter(list: List<LightColor>) {
        colorList.clear()
        colorList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorItem: View = itemView.findViewById(R.id.colorView)
        var colorSelectedItem: View = itemView.findViewById(R.id.colorViewSelected)
    }

    fun setSelectedItem(position: Int) {
        selectedPos = position
        notifyDataSetChanged()
    }

    companion object {
        var selectedPos = RecyclerView.NO_POSITION
    }

}