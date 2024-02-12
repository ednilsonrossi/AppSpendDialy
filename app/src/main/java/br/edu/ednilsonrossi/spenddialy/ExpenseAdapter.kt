package br.edu.ednilsonrossi.spenddialy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val dataset: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textDescription: TextView = itemView.findViewById(R.id.text_expense_desc)
        private val textValue: TextView = itemView.findViewById(R.id.text_expense_value)

        fun bind(expense: Expense){
            textDescription.text = expense.descripton
            textValue.text = "R$ ${expense.value}"
        }
    }

}