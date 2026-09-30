package com.profitcalc.tj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.profitcalc.tj.data.prefs.AppSettings
import com.profitcalc.tj.data.prefs.ThemeMode
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.i18n.stringsFor
import com.profitcalc.tj.navigation.ProfitCalcNavHost
import com.profitcalc.tj.ui.theme.ProfitCalcTheme
import com.profitcalc.tj.viewmodel.SettingsViewModel
import com.profitcalc.tj.viewmodel.appViewModel
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfitCalcRoot()
        }
    }
}

@Composable
private fun ProfitCalcRoot() {
    val settingsViewModel: SettingsViewModel = appViewModel()
    val settings: AppSettings by settingsViewModel.settings.collectAsState()

    val systemDark = isSystemInDarkTheme()
    val useDarkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemDark
    }

    CompositionLocalProvider(LocalAppStrings provides stringsFor(settings.language)) {
        ProfitCalcTheme(darkTheme = useDarkTheme) {
            ProfitCalcNavHost(settings = settings)
        }
    }
}
