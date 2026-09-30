package com.profitcalc.tj.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.profitcalc.tj.data.local.HistoryEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Writes export files into the app's cache directory and builds a
 * FileProvider-backed share Intent. Entirely offline — no network permission
 * is used or required for this.
 */
object CsvExporter {

    private fun exportsDir(context: Context): File =
        File(context.cacheDir, "exports").apply { mkdirs() }

    fun exportHistoryToCsv(context: Context, entries: List<HistoryEntity>): File {
        val file = File(exportsDir(context), "profitcalc_history_${timestampSuffix()}.csv")
        file.bufferedWriter().use { writer ->
            writer.write("Product,SalePrice,Quantity,ProfitPerItem,TotalProfit,Margin%,Date\n")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            entries.forEach { e ->
                writer.write(
                    "${csvEscape(e.productName)},${e.salePrice},${e.quantity}," +
                        "${e.profitPerItem},${e.totalProfit},${e.marginPercent}," +
                        "${dateFormat.format(Date(e.timestamp))}\n"
                )
            }
        }
        return file
    }

    fun exportHistoryToTxt(context: Context, entries: List<HistoryEntity>): File {
        val file = File(exportsDir(context), "profitcalc_history_${timestampSuffix()}.txt")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        file.bufferedWriter().use { writer ->
            entries.forEach { e ->
                writer.write(
                    "${e.productName}\n" +
                        "  ${dateFormat.format(Date(e.timestamp))}\n" +
                        "  Sale price: ${Formatters.money(e.salePrice)}  x${e.quantity}\n" +
                        "  Profit/item: ${Formatters.money(e.profitPerItem)}\n" +
                        "  Total profit: ${Formatters.money(e.totalProfit)}\n" +
                        "  Margin: ${Formatters.percent(e.marginPercent)}\n\n"
                )
            }
        }
        return file
    }

    fun exportSingleResultToTxt(context: Context, title: String, lines: List<Pair<String, String>>): File {
        val file = File(exportsDir(context), "profitcalc_result_${timestampSuffix()}.txt")
        file.bufferedWriter().use { writer ->
            writer.write("$title\n")
            writer.write("-".repeat(title.length) + "\n")
            lines.forEach { (label, value) -> writer.write("$label: $value\n") }
        }
        return file
    }

    fun shareIntentFor(context: Context, file: File): Intent {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        return Intent(Intent.ACTION_SEND).apply {
            type = if (file.extension == "csv") "text/csv" else "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun timestampSuffix(): String =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

    private fun csvEscape(value: String): String =
        if (value.contains(',') || value.contains('"')) {
            "\"${value.replace("\"", "\"\"")}\""
        } else value
}
