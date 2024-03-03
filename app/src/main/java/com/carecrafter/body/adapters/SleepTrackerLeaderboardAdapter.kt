package com.carecrafter.body.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carecrafter.R
import com.carecrafter.models.LeaderboardForSleepTracker
import com.carecrafter.models.LeaderboardForStepTracker

class SleepTrackerLeaderboardAdapter(private var entries: List<LeaderboardForSleepTracker>) :
    RecyclerView.Adapter<SleepTrackerLeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank: TextView = itemView.findViewById(R.id.rank)
        val name: TextView = itemView.findViewById(R.id.name)
        val sleeps: TextView = itemView.findViewById(R.id.sleeps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.leaderboard_for_sleep_tracker,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.rank.text = (position + 1).toString()
        holder.name.text = entry.name
        holder.sleeps.text = entry.sleeps
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateData(newEntries: List<LeaderboardForSleepTracker>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
