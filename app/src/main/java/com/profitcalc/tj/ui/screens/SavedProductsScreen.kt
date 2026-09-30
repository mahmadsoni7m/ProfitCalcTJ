package com.profitcalc.tj.ui.screens

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.profitcalc.tj.util.Formatters
import com.profitcalc.tj.viewmodel.ProductCalculatorViewModel
import com.profitcalc.tj.viewmodel.SavedProductsViewModel
import com.profitcalc.tj.viewmodel.appViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedProductsScreen(currencySymbol: String, decimalPlaces: Int, onLoadIntoCalculator: () -> Unit) {
    val strings = LocalAppStrings.current
    val viewModel: SavedProductsViewModel = appViewModel()
    val calculatorViewModel: ProductCalculatorViewModel = appViewModel()
    val products by viewModel.products.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text(strings.savedProductsTitle) }) }) { padding ->
        if (products.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
                Text(strings.savedProductsEmpty, style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(products, key = { it.id }) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            calculatorViewModel.loadFromSavedProduct(product)
                            onLoadIntoCalculator()
                        },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(product.name, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    Formatters.moneyWithCurrency(product.defaultSalePrice, currencySymbol, decimalPlaces),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            IconButton(onClick = { viewModel.delete(product) }) {
                                Icon(Icons.Filled.Delete, contentDescription = strings.savedProductsDelete)
                            }
                        }
                    }
                }
            }
        }
    }
}
