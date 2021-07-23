package com.engx1.thegympodtvapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.engx1.thegympodtvapp.R
import com.engx1.thegympodtvapp.model.InstructorSocialMedia

class SocialMediaAdapter (val clickListener: (InstructorSocialMedia) -> Unit): RecyclerView.Adapter<SocialMediaAdapter.ViewHolder>() {
    private val socialMediaList: MutableList<InstructorSocialMedia> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_social_media, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(socialMediaList[position].icon).into(holder.socialMediaIcon)

        holder.socialMediaLayout.setOnClickListener {
            clickListener(socialMediaList[position])
        }
    }

    fun updateAdapter(list: List<InstructorSocialMedia>) {
        socialMediaList.clear()
        socialMediaList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return socialMediaList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val socialMediaIcon: ImageView = itemView.findViewById(R.id.socialMediaIcon)
        val socialMediaLayout: RelativeLayout = itemView.findViewById(R.id.socialMediaLayout)
    }
}