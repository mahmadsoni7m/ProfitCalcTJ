package com.profitcalc.tj.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object Formatters {

    /** Formats a money value with a fixed decimal-point (not comma) separator, e.g. "500.00". */
    fun money(value: Double, decimalPlaces: Int = 2): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val pattern = if (decimalPlaces <= 0) "#,##0" else "#,##0." + "0".repeat(decimalPlaces)
        val format = DecimalFormat(pattern, symbols)
        return format.format(value)
    }

    fun moneyWithCurrency(value: Double, currencySymbol: String, decimalPlaces: Int = 2): String =
        "${money(value, decimalPlaces)} $currencySymbol"

    fun percent(value: Double, decimalPlaces: Int = 1): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val pattern = if (decimalPlaces <= 0) "#,##0" else "#,##0." + "0".repeat(decimalPlaces)
        val format = DecimalFormat(pattern, symbols)
        return "${format.format(value)}%"
    }

    fun multiplier(value: Double, decimalPlaces: Int = 2): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val pattern = "#,##0." + "0".repeat(decimalPlaces.coerceAtLeast(1))
        val format = DecimalFormat(pattern, symbols)
        return "${format.format(value)}x"
    }

    /** Parses user input that may use either '.' or ',' as decimal separator. Never throws. */
    fun parseInput(text: String): Double {
        if (text.isBlank()) return 0.0
        val normalized = text.replace(',', '.').filter { it.isDigit() || it == '.' || it == '-' }
        return normalized.toDoubleOrNull() ?: 0.0
    }
}
