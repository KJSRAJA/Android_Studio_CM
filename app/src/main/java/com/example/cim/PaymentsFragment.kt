package com.example.cim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class PaymentsFragment : Fragment() {

    private lateinit var adapter: PaymentAdapter
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payments, container, false)

        val rvPayments = view.findViewById<RecyclerView>(R.id.rvPayments)
        tvEmpty = view.findViewById(R.id.tvEmptyPayments)

        adapter = PaymentAdapter(getUnpaidTransactions()) { transaction ->
            transaction.isPaid = true
            updateList()
        }
        rvPayments.adapter = adapter
        
        updateList()

        return view
    }

    private fun getUnpaidTransactions(): List<DailyTransaction> {
        return DataRepository.transactions.filter { !it.isPaid }
    }

    private fun updateList() {
        val unpaid = getUnpaidTransactions()
        adapter.updateData(unpaid)
        tvEmpty.visibility = if (unpaid.isEmpty()) View.VISIBLE else View.GONE
    }
}
