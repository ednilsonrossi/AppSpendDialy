package br.edu.ednilsonrossi.spenddialy.model.entity

import java.time.LocalDate

class DayExpense(val dayExpenseValue: Double, val date: LocalDate){

    val expenses = mutableListOf<Expense>()
    var totalSpent: Double = 0.0


    fun newExpense(expense: Expense){
        totalSpent += expense.value
        expenses.add(expense)
    }

    fun getBalance() = dayExpenseValue - totalSpent


}