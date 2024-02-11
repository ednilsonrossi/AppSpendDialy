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
import br.edu.ednilsonrossi.spenddialy.model.entity.DayExpense
import br.edu.ednilsonrossi.spenddialy.model.entity.Expense
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var day: DayExpense

    private lateinit var daySpendText: TextView
    private lateinit var dateSpendText: TextView
    private lateinit var alredySpendText: TextView
    private lateinit var canSpendText: TextView
    private lateinit var newExpenseButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        daySpendText = findViewById(R.id.text_daily_spend)
        dateSpendText = findViewById(R.id.text_daily_date)
        alredySpendText = findViewById(R.id.text_alredy_spend)
        canSpendText = findViewById(R.id.text_can_spend)
        newExpenseButton = findViewById(R.id.button_new_expense)
        newExpenseButton.setOnClickListener(this)
        newExpenseButton.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_define_daily_spend -> {
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

        if (day != null) {

            daySpendText.text = "R$ ${day.dayExpenseValue}"
            val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale("pt", "BR"))
            dateSpendText.text = day.date.format(formatter)
            canSpendText.text = "R$ ${day.getBalance()}"
            alredySpendText.text = "R$ ${day.totalSpent}"

            newExpenseButton.isEnabled = true
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
                day.newExpense(Expense(value, description, LocalDate.now()))
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
                day = DayExpense(value.toDouble(), LocalDate.now())
                updateUI()
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

}