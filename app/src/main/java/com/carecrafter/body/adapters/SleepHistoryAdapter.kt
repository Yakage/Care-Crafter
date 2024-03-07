package com.carecrafter.body.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carecrafter.R
import com.carecrafter.models.LeaderboardForStepTracker
import com.carecrafter.models.SleepHistory
import com.carecrafter.models.StepHistory
import com.carecrafter.models.WaterHistory

class SleepHistoryAdapter (private var entries: List<SleepHistory>) :
    RecyclerView.Adapter<SleepHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tv_created_at)
        val sleepLogs: TextView = itemView.findViewById(R.id.tv_sleep_logs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_sleep_history,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.date.text = "${entry.date}: "
        holder.sleepLogs.text = "Slept for ${entry.sleeps} and \n score of ${entry.score}"
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateData(newEntries: List<SleepHistory>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}