package com.carecrafter.body.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carecrafter.R
import com.carecrafter.models.LeaderboardForStepTracker
import com.carecrafter.models.StepHistory

class StepHistoryAdapter (private var entries: List<StepHistory>) :
    RecyclerView.Adapter<StepHistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tv_created_at)
        val stepLogs: TextView = itemView.findViewById(R.id.tv_step_logs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_step_history,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.date.text = "${entry.created_at}: "
        holder.stepLogs.text = "${entry.current_steps} / ${entry.daily_goal}"
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateData(newEntries: List<StepHistory>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}