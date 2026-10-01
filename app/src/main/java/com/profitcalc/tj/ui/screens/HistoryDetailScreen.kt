package com.profitcalc.tj.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.data.local.HistoryEntity
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.ui.components.HeadlineResultCard
import com.profitcalc.tj.ui.components.SectionCard
import com.profitcalc.tj.ui.components.StatRow
import com.profitcalc.tj.util.Formatters
import com.profitcalc.tj.viewmodel.HistoryViewModel
import com.profitcalc.tj.viewmodel.appViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(historyId: Long, currencySymbol: String, decimalPlaces: Int) {
    val strings = LocalAppStrings.current
    val viewModel: HistoryViewModel = appViewModel()
    val items by viewModel.history.collectAsState()
    val entity: HistoryEntity? = items.firstOrNull { it.id == historyId }

    Scaffold(topBar = { TopAppBar(title = { Text(strings.historyDetailsTitle) }) }) { padding ->
        if (entity == null) {
            return@Scaffold
        }
        val result = remember(entity) { viewModel.recalculate(entity) }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(entity.productName, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            }
            item {
                HeadlineResultCard(
                    strings.resultProfitPerItem,
                    Formatters.moneyWithCurrency(result.profitPerItem, currencySymbol, decimalPlaces),
                    isPositive = result.profitPerItem >= 0,
                )
            }
            item {
                SectionCard(strings.sectionBreakdown) {
                    StatRow(strings.inputSalePrice, Formatters.money(entity.salePrice, decimalPlaces))
                    StatRow(strings.inputQuantity, entity.quantity.toString())
                    StatRow(strings.resultFinalSalePrice, Formatters.money(result.finalSalePrice, decimalPlaces))
                    StatRow(strings.resultTotalProfit, Formatters.money(result.totalProfit, decimalPlaces))
                    StatRow(strings.resultRevenue, Formatters.money(result.revenue, decimalPlaces))
                    StatRow(strings.resultTotalCommission, Formatters.money(result.totalCommission, decimalPlaces))
                    StatRow(strings.resultTotalCost, Formatters.money(result.totalCost, decimalPlaces))
                    StatRow(strings.resultMargin, Formatters.percent(result.marginPercent))
                    StatRow(strings.resultRoi, Formatters.percent(result.roiPercent))
                }
            }
        }
    }
}
