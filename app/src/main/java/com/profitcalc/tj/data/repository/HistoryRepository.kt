package com.profitcalc.tj.data.repository

import com.profitcalc.tj.data.local.HistoryDao
import com.profitcalc.tj.data.local.HistoryEntity
import com.profitcalc.tj.engine.AdvertisingMode
import com.profitcalc.tj.engine.CalculationInput
import com.profitcalc.tj.engine.CalculationResult
import com.profitcalc.tj.engine.CalculatorEngine
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class HistoryRepository(private val dao: HistoryDao) {

    fun observeAll(): Flow<List<HistoryEntity>> = dao.observeAll()

    fun observeToday(): Flow<List<HistoryEntity>> {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.observeSince(cal.timeInMillis)
    }

    suspend fun save(input: CalculationInput, result: CalculationResult): Long {
        val entity = HistoryEntity(
            productName = input.productName.ifBlank { "—" },
            timestamp = System.currentTimeMillis(),
            purchasePrice = input.purchasePrice,
            salePrice = input.salePrice,
            quantity = input.quantity,
            commissionPercent = input.commissionPercent,
            logistics = input.logistics,
            packaging = input.packaging,
            advertisingModeIsTotal = input.advertisingMode == AdvertisingMode.TOTAL_BUDGET,
            advertisingValue = input.advertisingValue,
            discountPercent = input.discountPercent,
            taxPercent = input.taxPercent,
            otherCosts = input.otherCosts,
            profitPerItem = result.profitPerItem,
            totalProfit = result.totalProfit,
            marginPercent = result.marginPercent,
        )
        return dao.insert(entity)
    }

    suspend fun duplicate(entity: HistoryEntity) {
        dao.insert(entity.copy(id = 0, timestamp = System.currentTimeMillis()))
    }

    suspend fun delete(entity: HistoryEntity) = dao.delete(entity)

    suspend fun clearAll() = dao.clearAll()

    suspend fun update(entity: HistoryEntity) = dao.update(entity)

    fun toCalculationInput(entity: HistoryEntity): CalculationInput = CalculationInput(
        productName = entity.productName,
        purchasePrice = entity.purchasePrice,
        salePrice = entity.salePrice,
        quantity = entity.quantity,
        commissionPercent = entity.commissionPercent,
        logistics = entity.logistics,
        packaging = entity.packaging,
        advertisingMode = if (entity.advertisingModeIsTotal) AdvertisingMode.TOTAL_BUDGET else AdvertisingMode.PER_ITEM,
        advertisingValue = entity.advertisingValue,
        discountPercent = entity.discountPercent,
        taxPercent = entity.taxPercent,
        otherCosts = entity.otherCosts,
    )

    fun recalculate(entity: HistoryEntity): CalculationResult =
        CalculatorEngine.calculate(toCalculationInput(entity))
}
