package br.edu.ednilsonrossi.spenddialy

import android.content.Context
import com.google.gson.Gson
import java.time.LocalDate

object ExpensePeriodSharedPreferences {

    private const val PREF_NAME = "spend_dialy"
    private const val KEY = "expense_period"
    private val gson = Gson()

    fun save(context: Context, expensePeriod: ExpensePeriod) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = gson.toJson(convertToSave(expensePeriod))
        editor.putString(KEY, json)
        editor.apply()
    }

    fun load(context: Context): ExpensePeriod? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY, null)
        return if (json != null) {
            val save = gson.fromJson(json, ExpenseSave::class.java)
            convertFromSave(save)
        } else {
            null
        }
    }

    private fun convertToSave(expense: ExpensePeriod): ExpenseSave {
        return ExpenseSave(
            expense.dayExpenseValue,
            expense.accumulated,
            expense.expenses,
            expense.date.dayOfMonth,
            expense.date.monthValue,
            expense.date.year
        )
    }

    private fun convertFromSave(save: ExpenseSave): ExpensePeriod {
        return ExpensePeriod(
            save.dayExpenseValue,
            save.accumulated,
            LocalDate.of(save.year, save.month, save.day),
            save.expenses
        )
    }
}

class ExpenseSave(
    val dayExpenseValue: Double,
    val accumulated: Double,
    val expenses: MutableList<Expense>,
    val day: Int,
    val month: Int,
    val year: Int
)