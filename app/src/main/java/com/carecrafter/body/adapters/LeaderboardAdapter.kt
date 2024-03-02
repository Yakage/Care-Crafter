package com.carecrafter.body.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carecrafter.R
import com.carecrafter.models.LeaderboardForStepTracker

class LeaderboardAdapter(private var entries: List<LeaderboardForStepTracker>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank: TextView = itemView.findViewById(R.id.rank)
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
        holder.name.text = entry.name
        holder.steps.text = entry.steps
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateData(newEntries: List<LeaderboardForStepTracker>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
