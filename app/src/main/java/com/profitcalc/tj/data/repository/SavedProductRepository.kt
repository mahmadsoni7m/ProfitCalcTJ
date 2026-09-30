package com.profitcalc.tj.data.repository

import com.profitcalc.tj.data.local.SavedProductDao
import com.profitcalc.tj.data.local.SavedProductEntity
import com.profitcalc.tj.engine.AdvertisingMode
import com.profitcalc.tj.engine.CalculationInput
import kotlinx.coroutines.flow.Flow

class SavedProductRepository(private val dao: SavedProductDao) {

    fun observeAll(): Flow<List<SavedProductEntity>> = dao.observeAll()

    suspend fun save(input: CalculationInput): Long {
        val entity = SavedProductEntity(
            name = input.productName.ifBlank { "—" },
            purchasePrice = input.purchasePrice,
            defaultSalePrice = input.salePrice,
            commissionPercent = input.commissionPercent,
            logistics = input.logistics,
            packaging = input.packaging,
            advertisingModeIsTotal = input.advertisingMode == AdvertisingMode.TOTAL_BUDGET,
            advertisingValue = input.advertisingValue,
            taxPercent = input.taxPercent,
            otherCosts = input.otherCosts,
        )
        return dao.insert(entity)
    }

    suspend fun delete(entity: SavedProductEntity) = dao.delete(entity)

    fun toCalculationInput(entity: SavedProductEntity): CalculationInput = CalculationInput(
        productName = entity.name,
        purchasePrice = entity.purchasePrice,
        salePrice = entity.defaultSalePrice,
        commissionPercent = entity.commissionPercent,
        logistics = entity.logistics,
        packaging = entity.packaging,
        advertisingMode = if (entity.advertisingModeIsTotal) AdvertisingMode.TOTAL_BUDGET else AdvertisingMode.PER_ITEM,
        advertisingValue = entity.advertisingValue,
        taxPercent = entity.taxPercent,
        otherCosts = entity.otherCosts,
    )
}
