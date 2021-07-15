package com.engx1.thegympodtvapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.`interface`.MusicClickListener
import com.engx1.thegympodtvapp.model.MusicResponse

class MusicAdapter(private val clickListener: MusicClickListener): RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    private val musicList: MutableList<MusicResponse> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val streamName: TextView = itemView.findViewById(R.id.streamTitleTV)
        val currentStream: TextView = itemView.findViewById(R.id.currentStreamTV)
        val streamView: RelativeLayout = itemView.findViewById(R.id.streamItemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_music_playlist, parent, false)
        return ViewHolder(view)
    }

    fun updateAdapter(list: List<MusicResponse>) {
        musicList.clear()
        musicList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.streamName.text = musicList[position].title

        holder.streamView.setOnClickListener {
            clickListener.onClicked(musicList[position], position)
        }

        if (selectedPos == position) {
            holder.currentStream.visibility = View.VISIBLE
        } else {
            holder.currentStream.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    fun setSelectedItem(position: Int) {
        selectedPos = position
        notifyDataSetChanged()
    }

    companion object {
        var selectedPos = RecyclerView.NO_POSITION
    }
}