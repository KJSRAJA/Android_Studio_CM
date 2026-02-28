package com.example.cim

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID

class AgentsFragment : Fragment() {

    private lateinit var adapter: AgentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agents, container, false)

        val rvAgents = view.findViewById<RecyclerView>(R.id.rvAgents)
        val fabAddAgent = view.findViewById<FloatingActionButton>(R.id.fabAddAgent)

        adapter = AgentAdapter(DataRepository.agents) { agent ->
            DataRepository.agents.remove(agent)
            adapter.updateData(ArrayList(DataRepository.agents))
        }
        rvAgents.adapter = adapter

        fabAddAgent.setOnClickListener {
            showAddAgentDialog()
        }

        return view
    }

    private fun showAddAgentDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_agent, null)
        val etName = dialogView.findViewById<EditText>(R.id.etAgentName)
        val etArea = dialogView.findViewById<EditText>(R.id.etAgentArea)
        val etContact = dialogView.findViewById<EditText>(R.id.etAgentContact)

        AlertDialog.Builder(context)
            .setTitle("Add New Agent")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString()
                val area = etArea.text.toString()
                val contact = etContact.text.toString()

                if (name.isNotEmpty() && area.isNotEmpty()) {
                    val newAgent = Agent(UUID.randomUUID().toString(), name, area, contact.ifEmpty { null })
                    DataRepository.agents.add(newAgent)
                    adapter.updateData(ArrayList(DataRepository.agents))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
