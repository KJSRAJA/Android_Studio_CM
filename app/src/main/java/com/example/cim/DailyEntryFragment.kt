package com.example.cim

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class DailyEntryFragment : Fragment() {

    private var selectedDate: Date = Date()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_entry, container, false)

        val spinnerAgent = view.findViewById<Spinner>(R.id.spinnerAgent)
        val btnPickDate = view.findViewById<Button>(R.id.btnPickDate)
        val tvSelectedDate = view.findViewById<TextView>(R.id.tvSelectedDate)
        val etTaken = view.findViewById<TextInputEditText>(R.id.etTaken)
        val etReturned = view.findViewById<TextInputEditText>(R.id.etReturned)
        val tvSold = view.findViewById<TextView>(R.id.tvSold)
        val tvPayable = view.findViewById<TextView>(R.id.tvPayable)
        val tvCommission = view.findViewById<TextView>(R.id.tvCommission)
        val btnSaveEntry = view.findViewById<Button>(R.id.btnSaveEntry)

        // Setup Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, DataRepository.agents)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAgent.adapter = adapter

        // Setup Date Picker
        tvSelectedDate.text = dateFormat.format(selectedDate)
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                tvSelectedDate.text = dateFormat.format(selectedDate)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val calculationWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, min: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val taken = etTaken.text.toString().toIntOrNull() ?: 0
                val returned = etReturned.text.toString().toIntOrNull() ?: 0
                val sold = taken - returned
                
                if (sold < 0) {
                    tvSold.text = "Sold: Error (Returned > Taken)"
                    return
                }

                val payable = sold * DataRepository.COMPANY_PRICE
                val commission = sold * (DataRepository.SELLING_PRICE - DataRepository.COMPANY_PRICE)

                tvSold.text = "Sold: $sold"
                tvPayable.text = "Payable to Company: ₹${String.format("%.2f", payable)}"
                tvCommission.text = "Agent Commission: ₹${String.format("%.2f", commission)}"
            }
        }

        etTaken.addTextChangedListener(calculationWatcher)
        etReturned.addTextChangedListener(calculationWatcher)

        btnSaveEntry.setOnClickListener {
            val agent = spinnerAgent.selectedItem as? Agent
            val taken = etTaken.text.toString().toIntOrNull() ?: 0
            val returned = etReturned.text.toString().toIntOrNull() ?: 0

            if (agent == null) {
                Toast.makeText(context, "Please add an agent first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (taken - returned < 0) {
                Toast.makeText(context, "Returned cannot be more than taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transaction = DailyTransaction(
                UUID.randomUUID().toString(),
                agent,
                selectedDate,
                taken,
                returned,
                DataRepository.COMPANY_PRICE,
                DataRepository.SELLING_PRICE
            )

            DataRepository.transactions.add(transaction)
            Toast.makeText(context, "Entry Saved!", Toast.LENGTH_SHORT).show()
            
            // Clear inputs
            etTaken.text?.clear()
            etReturned.text?.clear()
        }

        return view
    }
}
