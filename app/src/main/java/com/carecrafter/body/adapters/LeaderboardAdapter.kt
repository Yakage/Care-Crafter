package com.carecrafter.body.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carecrafter.R
import com.carecrafter.models.LeaderboardForStepTracker

class LeaderboardAdapter(private var entries: List<LeaderboardForStepTracker>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank: TextView = itemView.findViewById(R.id.rank)
        val profilePicture: ImageView = itemView.findViewById(R.id.profileImage)
        val name: TextView = itemView.findViewById(R.id.name)
        val steps: TextView = itemView.findViewById(R.id.steps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.leaderboard_for_step_tracker,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.rank.text = (position + 1).toString()

        when (entry.avatar) {
            1 -> holder.profilePicture.setImageResource(R.drawable.avatarone)
            2 -> holder.profilePicture.setImageResource(R.drawable.avatartwo)
            3 -> holder.profilePicture.setImageResource(R.drawable.avatarthree)
            4 -> holder.profilePicture.setImageResource(R.drawable.avatarfour)
            5 -> holder.profilePicture.setImageResource(R.drawable.avatarfive)
            6 -> holder.profilePicture.setImageResource(R.drawable.avatarsix)
            7 -> holder.profilePicture.setImageResource(R.drawable.avatarseven)
            8 -> holder.profilePicture.setImageResource(R.drawable.avataeight)
            else -> {
                holder.profilePicture.setImageResource(R.drawable.boy_unpressed)
            }
        }

        holder.name.text = entry.name
        holder.steps.text = entry.steps.toString() + " Steps "
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateData(newEntries: List<LeaderboardForStepTracker>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
