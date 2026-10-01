package com.profitcalc.tj.ui.screens

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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.profitcalc.tj.ui.components.DashboardStatCard
import com.profitcalc.tj.util.Formatters
import com.profitcalc.tj.viewmodel.DashboardViewModel
import com.profitcalc.tj.viewmodel.appViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    currencySymbol: String,
    onNavigateToCalculator: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSavedProducts: () -> Unit,
    onNavigateToQuickCalc: () -> Unit,
    onNavigateToDiscount: () -> Unit = {},
    onNavigateToRoas: () -> Unit = {},
) {
    val strings = LocalAppStrings.current
    val viewModel: DashboardViewModel = appViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(strings.navDashboard) }) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(strings.appTagline, style = MaterialTheme.typography.bodyMedium)
            }

            if (!state.hasData) {
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Text(
                            strings.dashboardNoData,
                            modifier = Modifier.padding(24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            } else {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashboardStatCard(
                            strings.dashboardTodayProfit,
                            Formatters.moneyWithCurrency(state.todayProfit, currencySymbol),
                            modifier = Modifier.weight(1f),
                        )
                        DashboardStatCard(
                            strings.dashboardTodayCalculations,
                            state.todayCalculations.toString(),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashboardStatCard(
                            strings.dashboardSavedProducts,
                            state.savedProductsCount.toString(),
                            modifier = Modifier.weight(1f),
                        )
                        DashboardStatCard(
                            strings.dashboardAverageMargin,
                            Formatters.percent(state.averageMargin),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            item {
                Text(strings.dashboardQuickActions, style = MaterialTheme.typography.titleMedium)
            }
            item {
                QuickActionButton(strings.navCalculator, Icons.Filled.Calculate, onNavigateToCalculator)
            }
            item {
                QuickActionButton(strings.navQuickCalc, Icons.Filled.PointOfSale, onNavigateToQuickCalc)
            }
            item {
                QuickActionButton(strings.navHistory, Icons.Filled.History, onNavigateToHistory)
            }
            item {
                QuickActionButton(strings.navSavedProducts, Icons.Filled.Inventory2, onNavigateToSavedProducts)
            }
            item {
                QuickActionButton(strings.navDiscount, Icons.Filled.PointOfSale, onNavigateToDiscount)
            }
            item {
                QuickActionButton(strings.navRoas, Icons.Filled.PointOfSale, onNavigateToRoas)
            }
        }
    }
}

@Composable
private fun QuickActionButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text(label)
    }
}
