package com.profitcalc.tj.ui.screens

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
import com.profitcalc.tj.ui.components.CalculateButton
import com.profitcalc.tj.ui.components.HeadlineResultCard
import com.profitcalc.tj.ui.components.NumberField
import com.profitcalc.tj.ui.components.SectionCard
import com.profitcalc.tj.ui.components.StatRow
import com.profitcalc.tj.util.Formatters
import com.profitcalc.tj.viewmodel.ProductCalculatorViewModel
import com.profitcalc.tj.viewmodel.appViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreakEvenScreen(currencySymbol: String, decimalPlaces: Int) {
    val strings = LocalAppStrings.current
    val viewModel: ProductCalculatorViewModel = appViewModel()
    val input by viewModel.input.collectAsState()
    val breakEven = viewModel.breakEven(input)

    Scaffold(topBar = { TopAppBar(title = { Text(strings.breakEvenTitle) }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (!breakEven.achievable) {
                item {
                    Text(strings.breakEvenNotAchievable, color = MaterialTheme.colorScheme.error)
                }
            } else {
                item {
                    HeadlineResultCard(
                        strings.breakEvenMinPrice,
                        Formatters.moneyWithCurrency(breakEven.minSalePrice, currencySymbol, decimalPlaces),
                    )
                }
                item {
                    SectionCard(strings.sectionBreakdown) {
                        StatRow(
                            strings.breakEvenProfitAtCurrentPrice,
                            Formatters.money(breakEven.profitAtCurrentPrice, decimalPlaces),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetProfitScreen(currencySymbol: String, decimalPlaces: Int) {
    val strings = LocalAppStrings.current
    val viewModel: ProductCalculatorViewModel = appViewModel()
    val input by viewModel.input.collectAsState()
    val target by viewModel.targetProfitTotal.collectAsState()
    val result = viewModel.targetProfit(input, target)

    Scaffold(topBar = { TopAppBar(title = { Text(strings.targetProfitTitle) }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                NumberField(strings.targetProfitInput, target, viewModel::setTargetProfitTotal)
            }
            item { CalculateButton() }
            if (!result.achievable) {
                item {
                    Text(strings.breakEvenNotAchievable, color = MaterialTheme.colorScheme.error)
                }
            } else {
                item {
                    HeadlineResultCard(
                        strings.targetProfitRequiredPriceLabel,
                        Formatters.moneyWithCurrency(result.requiredSalePrice, currencySymbol, decimalPlaces),
                    )
                }
                item {
                    Text(
                        strings.targetProfitResultTemplate.format(
                            Formatters.moneyWithCurrency(result.requiredSalePrice, currencySymbol, decimalPlaces),
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}
