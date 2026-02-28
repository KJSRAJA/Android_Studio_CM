package com.example.cim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AgentAdapter(
    private var agents: List<Agent>,
    private val onDeleteClick: (Agent) -> Unit
) : RecyclerView.Adapter<AgentAdapter.AgentViewHolder>() {

    class AgentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAgentName)
        val tvArea: TextView = view.findViewById(R.id.tvAgentArea)
        val tvContact: TextView = view.findViewById(R.id.tvAgentContact)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteAgent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agent, parent, false)
        return AgentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        val agent = agents[position]
        holder.tvName.text = agent.name
        holder.tvArea.text = agent.area
        holder.tvContact.text = agent.contact ?: "No contact"
        holder.btnDelete.setOnClickListener { onDeleteClick(agent) }
    }

    override fun getItemCount(): Int = agents.size

    fun updateData(newAgents: List<Agent>) {
        agents = newAgents
        notifyDataSetChanged()
    }
}
