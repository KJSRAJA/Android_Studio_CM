package com.example.cim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class PaymentAdapter(
    private var transactions: List<DailyTransaction>,
    private val onMarkPaidClick: (DailyTransaction) -> Unit
) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    class PaymentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAgent: TextView = view.findViewById(R.id.tvPaymentAgent)
        val tvDate: TextView = view.findViewById(R.id.tvPaymentDate)
        val tvDetails: TextView = view.findViewById(R.id.tvPaymentDetails)
        val btnMarkPaid: Button = view.findViewById(R.id.btnMarkPaid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.tvAgent.text = transaction.agent.name
        holder.tvDate.text = dateFormat.format(transaction.date)
        holder.tvDetails.text = "Sold: ${transaction.sold} | Payable: ₹${String.format("%.2f", transaction.payableAmount)}"
        
        holder.btnMarkPaid.setOnClickListener { onMarkPaidClick(transaction) }
    }

    override fun getItemCount(): Int = transactions.size

    fun updateData(newTransactions: List<DailyTransaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
