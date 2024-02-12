package br.edu.ednilsonrossi.spenddialy

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity(), OnClickListener {

    private val PERIOD_DATE = "date"
    private val DAY_EXPENSE_VALUE = "day_expense_value"
    private val ACCUMULATE = "acumulate"


    private lateinit var period: ExpensePeriod

    private lateinit var textDaySpend: TextView
    private lateinit var textDateSpend: TextView
    private lateinit var textAlredySpend: TextView
    private lateinit var textCanSpend: TextView
    private lateinit var buttonNewExpense: Button
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        textDaySpend = findViewById(R.id.text_daily_spend)
        textDateSpend = findViewById(R.id.text_daily_date)
        textAlredySpend = findViewById(R.id.text_alredy_spend)
        textCanSpend = findViewById(R.id.text_can_spend)
        buttonNewExpense = findViewById(R.id.button_new_expense)
        buttonNewExpense.setOnClickListener(this)
        buttonNewExpense.isEnabled = false

        configRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadPrefs()

        if(::period.isInitialized) {
            val data = period.date
            val today = LocalDate.now()
            if (data < today) {
                val days = Period.between(data, today).days
                period = ExpensePeriod(period.dayExpenseValue, period.getBalance() + ( (days-1) * period.dayExpenseValue), today)
            }
        }
        updateUI()
    }

    override fun onStop() {
        super.onStop()
        ExpensePeriodSharedPreferences.save(this, period)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_init_period -> {
                showDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_new_expense) {
            showExpenseDialog()
        }
    }

    private fun updateUI() {

        if (::period.isInitialized) {

            textDaySpend.text = "R$ ${period.dayExpenseValue}"
            val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale("pt", "BR"))
            textDateSpend.text = period.date.format(formatter)
            textCanSpend.text = "R$ ${period.getBalance()}"
            textAlredySpend.text = "R$ ${period.getTotalSpent()}"

            buttonNewExpense.isEnabled = true
            recyclerView.adapter = ExpenseAdapter(period.expenses)
        }
    }

    private fun configRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (::period.isInitialized){
            recyclerView.adapter = ExpenseAdapter(period.expenses)
        }
    }

    private fun showExpenseDialog() {
        val dialogView = layoutInflater.inflate(R.layout.insert_expense_dialog, null)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.edit_description_expense)
        val valueEditText = dialogView.findViewById<EditText>(R.id.edit_value_expense)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.expense_registry))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.registry)) { dialogInterface: DialogInterface, _: Int ->
                val description = descriptionEditText.text.toString()
                val value = valueEditText.text.toString().toDouble()
                period.newExpense(Expense(value, description))
                updateUI()
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.insert_spend_value_dialog, null)
        val editTextValue = dialogView.findViewById<EditText>(R.id.edit_daily_spend_value)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.limit_daily_spend))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.registry)) { dialogInterface: DialogInterface, _: Int ->
                val value = editTextValue.text.toString()
                period = ExpensePeriod(value.toDouble(), 0.0, LocalDate.now())
                configRecyclerView()
                updateUI()
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun loadPrefs(){
        val obj = ExpensePeriodSharedPreferences.load(this)
        if (obj != null){
            period = obj
        }
    }
}