package com.profitcalc.tj.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.data.prefs.ThemeMode
import com.profitcalc.tj.i18n.AppLanguage
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.ui.components.SectionCard
import com.profitcalc.tj.viewmodel.HistoryViewModel
import com.profitcalc.tj.viewmodel.SettingsViewModel
import com.profitcalc.tj.viewmodel.appViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val strings = LocalAppStrings.current
    val viewModel: SettingsViewModel = appViewModel()
    val historyViewModel: HistoryViewModel = appViewModel()
    val settings by viewModel.settings.collectAsState()
    var showClearConfirm by remember { mutableStateOf(false) }
    var currencyText by remember(settings.currencySymbol) { mutableStateOf(settings.currencySymbol) }

    Scaffold(topBar = { TopAppBar(title = { Text(strings.settingsTitle) }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                SectionCard(strings.settingsLanguage) {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = settings.language == AppLanguage.TAJIK,
                            onClick = { viewModel.setLanguage(AppLanguage.TAJIK) },
                            shape = SegmentedButtonDefaults.itemShape(0, 2),
                        ) { Text("Тоҷикӣ") }
                        SegmentedButton(
                            selected = settings.language == AppLanguage.RUSSIAN,
                            onClick = { viewModel.setLanguage(AppLanguage.RUSSIAN) },
                            shape = SegmentedButtonDefaults.itemShape(1, 2),
                        ) { Text("Русский") }
                    }
                }
            }

            item {
                SectionCard(strings.settingsTheme) {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = settings.themeMode == ThemeMode.LIGHT,
                            onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) },
                            shape = SegmentedButtonDefaults.itemShape(0, 3),
                        ) { Text(strings.settingsThemeLight) }
                        SegmentedButton(
                            selected = settings.themeMode == ThemeMode.DARK,
                            onClick = { viewModel.setThemeMode(ThemeMode.DARK) },
                            shape = SegmentedButtonDefaults.itemShape(1, 3),
                        ) { Text(strings.settingsThemeDark) }
                        SegmentedButton(
                            selected = settings.themeMode == ThemeMode.SYSTEM,
                            onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) },
                            shape = SegmentedButtonDefaults.itemShape(2, 3),
                        ) { Text(strings.settingsThemeSystem) }
                    }
                }
            }

            item {
                SectionCard(strings.settingsCurrency) {
                    OutlinedTextField(
                        value = currencyText,
                        onValueChange = {
                            currencyText = it
                            viewModel.setCurrencySymbol(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                }
            }

            item {
                SectionCard(strings.settingsDecimalPlaces) {
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        listOf(0, 1, 2, 3).forEachIndexed { index, places ->
                            SegmentedButton(
                                selected = settings.decimalPlaces == places,
                                onClick = { viewModel.setDecimalPlaces(places) },
                                shape = SegmentedButtonDefaults.itemShape(index, 4),
                            ) { Text(places.toString()) }
                        }
                    }
                }
            }

            item {
                SectionCard(strings.settingsClearHistory) {
                    TextButton(onClick = { showClearConfirm = true }) {
                        Text(strings.historyClearAll, color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            item {
                SectionCard(strings.settingsAbout) {
                    Text(strings.appNameTj, style = MaterialTheme.typography.titleMedium)
                    Text(strings.settingsAboutBody, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${strings.settingsVersion}: 1.0.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    historyViewModel.clearAll()
                    showClearConfirm = false
                }) { Text(strings.actionConfirm) }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text(strings.actionCancel) }
            },
        )
    }
}
