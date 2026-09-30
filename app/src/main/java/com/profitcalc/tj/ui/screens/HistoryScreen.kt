package com.profitcalc.tj.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.data.local.HistoryEntity
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.ui.theme.ProfitNegative
import com.profitcalc.tj.ui.theme.ProfitPositive
import com.profitcalc.tj.util.CsvExporter
import com.profitcalc.tj.util.Formatters
import com.profitcalc.tj.viewmodel.HistoryViewModel
import com.profitcalc.tj.viewmodel.appViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(currencySymbol: String, decimalPlaces: Int, onOpenDetail: (Long) -> Unit) {
    val strings = LocalAppStrings.current
    val viewModel: HistoryViewModel = appViewModel()
    val items by viewModel.history.collectAsState()
    val context = LocalContext.current
    var showClearConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.historyTitle) },
                actions = {
                    if (items.isNotEmpty()) {
                        IconButton(onClick = {
                            val file = CsvExporter.exportHistoryToCsv(context, items)
                            context.startActivity(
                                android.content.Intent.createChooser(
                                    CsvExporter.shareIntentFor(context, file), strings.exportCsv,
                                ),
                            )
                        }) {
                            Icon(Icons.Filled.IosShare, contentDescription = strings.exportCsv)
                        }
                        IconButton(onClick = { showClearConfirm = true }) {
                            Icon(Icons.Filled.DeleteSweep, contentDescription = strings.historyClearAll)
                        }
                    }
                },
            )
        },
    ) { padding ->
        if (items.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            ) {
                Text(strings.historyEmpty, style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(items, key = { it.id }) { entity ->
                    HistoryRow(
                        entity = entity,
                        currencySymbol = currencySymbol,
                        decimalPlaces = decimalPlaces,
                        onClick = { onOpenDetail(entity.id) },
                        onDelete = { viewModel.delete(entity) },
                        onDuplicate = { viewModel.duplicate(entity) },
                    )
                }
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text(strings.historyClearConfirmTitle) },
            text = { Text(strings.historyClearConfirmMessage) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAll()
                    showClearConfirm = false
                }) { Text(strings.actionConfirm) }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text(strings.actionCancel) }
            },
        )
    }
}

@Composable
private fun HistoryRow(
    entity: HistoryEntity,
    currencySymbol: String,
    decimalPlaces: Int,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
) {
    val strings = LocalAppStrings.current
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US) }
    val profitColor = if (entity.profitPerItem >= 0) ProfitPositive else ProfitNegative

    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(Modifier.weight(1f)) {
                Text(entity.productName, style = MaterialTheme.typography.titleMedium)
                Text(
                    dateFormat.format(Date(entity.timestamp)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    Formatters.moneyWithCurrency(entity.totalProfit, currencySymbol, decimalPlaces),
                    style = MaterialTheme.typography.bodyLarge,
                    color = profitColor,
                )
            }
            Row {
                IconButton(onClick = onDuplicate) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = strings.historyDuplicate)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = strings.historyDelete)
                }
            }
        }
    }
}
