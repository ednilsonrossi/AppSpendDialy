package br.edu.ednilsonrossi.spenddialy

import java.time.LocalDate

class ExpensePeriod(val dayExpenseValue: Double, val accumulated: Double, val date: LocalDate) {

    constructor(
        dayExpenseValue: Double,
        accumulated: Double,
        date: LocalDate,
        expensesList: List<Expense>
    ) : this(dayExpenseValue, accumulated, date) {
        expenses.addAll(expensesList)
    }

    val expenses = mutableListOf<Expense>()

    fun newExpense(expense: Expense) {
        expenses.add(expense)
    }

    fun getTotalSpent(): Double {
        return expenses.sumOf { it.value }
    }

    fun getBalance() = dayExpenseValue + accumulated - getTotalSpent()

}