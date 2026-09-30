package com.profitcalc.tj

import android.app.Application
import com.profitcalc.tj.data.local.AppDatabase
import com.profitcalc.tj.data.prefs.SettingsPrefs
import com.profitcalc.tj.data.repository.HistoryRepository
import com.profitcalc.tj.data.repository.SavedProductRepository

class ProfitCalcApp : Application() {

    lateinit var historyRepository: HistoryRepository
        private set
    lateinit var savedProductRepository: SavedProductRepository
        private set
    lateinit var settingsPrefs: SettingsPrefs
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        historyRepository = HistoryRepository(db.historyDao())
        savedProductRepository = SavedProductRepository(db.savedProductDao())
        settingsPrefs = SettingsPrefs(this)
    }
}
