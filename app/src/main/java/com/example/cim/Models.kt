package com.example.cim

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
    var isPaid: Boolean = false
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
}
