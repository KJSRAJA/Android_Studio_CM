package com.example.cim

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

data class Agent(
    val id: String,
    val name: String,
    val area: String,
    val contact: String?
) {
    override fun toString(): String = name
}

data class DailyTransaction(
    val id: String,
    val agent: Agent,
    val date: Date,
    val taken: Int,
    val returned: Int,
    val companyPrice: Double,
    val sellingPrice: Double,
    var isPaid: Boolean = false,
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val sold: Int get() = taken - returned
    val payableAmount: Double get() = sold * companyPrice
    val commission: Double get() = sold * (sellingPrice - companyPrice)
}

object DataRepository {
    val agents = mutableListOf<Agent>()
    val transactions = mutableListOf<DailyTransaction>()

    const val COMPANY_PRICE = 4.0
    const val SELLING_PRICE = 5.0

    private const val PREFS_NAME = "CiMPrefs"
    private const val AGENTS_KEY = "agents_list"
    private const val TRANSACTIONS_KEY = "transactions_list"
    private val gson = Gson()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Load Agents
        val agentsJson = prefs.getString(AGENTS_KEY, null)
        if (agentsJson != null) {
            val type = object : TypeToken<MutableList<Agent>>() {}.type
            agents.clear()
            agents.addAll(gson.fromJson(agentsJson, type))
        }

        // Load Transactions
        val transJson = prefs.getString(TRANSACTIONS_KEY, null)
        if (transJson != null) {
            val type = object : TypeToken<MutableList<DailyTransaction>>() {}.type
            transactions.clear()
            transactions.addAll(gson.fromJson(transJson, type))
        }
    }

    fun save(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(AGENTS_KEY, gson.toJson(agents))
        editor.putString(TRANSACTIONS_KEY, gson.toJson(transactions))
        editor.apply()
    }
}
