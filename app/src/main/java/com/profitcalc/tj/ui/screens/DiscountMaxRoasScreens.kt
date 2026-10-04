package com.profitcalc.tj.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.ui.components.HeadlineResultCard
import com.profitcalc.tj.ui.components.NumberField
import com.profitcalc.tj.util.Formatters
import com.profitcalc.tj.viewmodel.DiscountToolViewModel
import com.profitcalc.tj.viewmodel.ProductCalculatorViewModel
import com.profitcalc.tj.viewmodel.RoasViewModel
import com.profitcalc.tj.viewmodel.appViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DiscountScreen(currencySymbol: String, decimalPlaces: Int) {
    val strings = LocalAppStrings.current
    val viewModel: DiscountToolViewModel = appViewModel()
    val originalPrice by viewModel.originalPrice.collectAsState()
    val discountPercent by viewModel.discountPercent.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text(strings.discountTitle) }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            stickyHeader {
                HeadlineResultCard(
                    strings.discountFinalPrice,
                    Formatters.moneyWithCurrency(
                        viewModel.finalPrice(originalPrice, discountPercent), currencySymbol, decimalPlaces,
                    ),
                )
            }
            item { NumberField(strings.discountOriginalPrice, originalPrice, viewModel::setOriginalPrice) }
            item { NumberField(strings.discountPercent, discountPercent, viewModel::setDiscountPercent, suffix = "%") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MaxDiscountScreen(currencySymbol: String, decimalPlaces: Int) {
    val strings = LocalAppStrings.current
    val viewModel: ProductCalculatorViewModel = appViewModel()
    val input by viewModel.input.collectAsState()
    val minProfit by viewModel.minDesiredProfitForDiscount.collectAsState()
    val result = viewModel.maxDiscount(input, minProfit)

    Scaffold(topBar = { TopAppBar(title = { Text(strings.maxDiscountTitle) }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (result.achievable) {
                stickyHeader {
                    HeadlineResultCard(
                        strings.maxDiscountResultLabel,
                        Formatters.percent(result.maxDiscountPercent),
                    )
                }
            } else {
                item { Text(strings.maxDiscountNotAchievable, color = MaterialTheme.colorScheme.error) }
            }
            item {
                NumberField(strings.maxDiscountMinProfit, minProfit, viewModel::setMinDesiredProfitForDiscount)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RoasScreen() {
    val strings = LocalAppStrings.current
    val viewModel: RoasViewModel = appViewModel()
    val spend by viewModel.spend.collectAsState()
    val revenue by viewModel.revenue.collectAsState()
    val result = viewModel.result(spend, revenue)

    Scaffold(topBar = { TopAppBar(title = { Text(strings.roasTitle) }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (result.achievable) {
                stickyHeader {
                    HeadlineResultCard(strings.roasResultLabel, Formatters.multiplier(result.roas))
                }
                item {
                    Text(
                        strings.roasExplanationTemplate.format(
                            Formatters.multiplier(result.roas), Formatters.money(result.roas),
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                item { Text(strings.roasNotAchievable, color = MaterialTheme.colorScheme.error) }
            }
            item { NumberField(strings.roasSpend, spend, viewModel::setSpend) }
            item { NumberField(strings.roasRevenue, revenue, viewModel::setRevenue) }
        }
    }
}
