package com.profitcalc.tj.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.profitcalc.tj.ProfitCalcApp

/** Minimal manual DI: builds each ViewModel with the repositories owned by [ProfitCalcApp]. */
class AppViewModelFactory(private val app: ProfitCalcApp) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ProductCalculatorViewModel::class.java -> ProductCalculatorViewModel(
                app.historyRepository, app.savedProductRepository,
            ) as T
            HistoryViewModel::class.java -> HistoryViewModel(app.historyRepository) as T
            SavedProductsViewModel::class.java -> SavedProductsViewModel(app.savedProductRepository) as T
            SettingsViewModel::class.java -> SettingsViewModel(app.settingsPrefs) as T
            DashboardViewModel::class.java -> DashboardViewModel(app.historyRepository, app.savedProductRepository) as T
            DiscountToolViewModel::class.java -> DiscountToolViewModel() as T
            RoasViewModel::class.java -> RoasViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}

@Composable
fun rememberAppViewModelFactory(): AppViewModelFactory {
    val app = LocalContext.current.applicationContext as ProfitCalcApp
    return AppViewModelFactory(app)
}

@Composable
inline fun <reified T : ViewModel> appViewModel(): T {
    val factory = rememberAppViewModelFactory()
    return viewModel(factory = factory)
}
