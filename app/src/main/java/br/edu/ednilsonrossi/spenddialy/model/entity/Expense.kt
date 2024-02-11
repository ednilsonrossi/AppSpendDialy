package br.edu.ednilsonrossi.spenddialy.model.entity

import java.time.LocalDate

data class Expense(val value: Double, val descripton: String, val date: LocalDate)