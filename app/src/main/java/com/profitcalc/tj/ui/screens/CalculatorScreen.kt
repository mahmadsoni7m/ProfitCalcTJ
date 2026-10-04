package com.profitcalc.tj.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.engine.ValidationField
import com.profitcalc.tj.engine.ValidationSeverity
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.ui.components.AdvertisingModeToggle
import com.profitcalc.tj.ui.components.HeadlineResultCard
import com.profitcalc.tj.ui.components.IntField
import com.profitcalc.tj.ui.components.NumberField
import com.profitcalc.tj.ui.components.SectionCard
import com.profitcalc.tj.ui.components.StatRow
import com.profitcalc.tj.util.CsvExporter
import com.profitcalc.tj.util.Formatters
import com.profitcalc.tj.viewmodel.ProductCalculatorViewModel
import com.profitcalc.tj.viewmodel.appViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalculatorScreen(
    currencySymbol: String,
    decimalPlaces: Int,
    onOpenBreakEven: () -> Unit = {},
    onOpenTargetProfit: () -> Unit = {},
    onOpenMaxDiscount: () -> Unit = {},
) {
    val strings = LocalAppStrings.current
    val viewModel: ProductCalculatorViewModel = appViewModel()
    val input by viewModel.input.collectAsState()
    val formVersion by viewModel.formVersion.collectAsState()
    val context = LocalContext.current

    val issues = viewModel.validate(input)
    fun issueFor(field: ValidationField) = issues.firstOrNull { it.field == field }
    val hasBlockingErrors = issues.any { it.severity == ValidationSeverity.ERROR }
    val result = if (!hasBlockingErrors) viewModel.calculate(input) else null

    Scaffold(topBar = { TopAppBar(title = { Text(strings.navCalculator) }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (result != null) {
                stickyHeader {
                    HeadlineResultCard(
                        strings.resultProfitPerItem,
                        Formatters.moneyWithCurrency(result.profitPerItem, currencySymbol, decimalPlaces),
                        isPositive = result.profitPerItem >= 0,
                    )
                }
                item {
                    SectionCard(strings.sectionBreakdown) {
                        StatRow(strings.resultFinalSalePrice, Formatters.money(result.finalSalePrice, decimalPlaces))
                        StatRow(strings.resultDiscountAmount, Formatters.money(result.discountAmount, decimalPlaces))
                        StatRow(strings.resultTotalProfit, Formatters.money(result.totalProfit, decimalPlaces))
                        StatRow(strings.resultRevenue, Formatters.money(result.revenue, decimalPlaces))
                        StatRow(strings.resultTotalCommission, Formatters.money(result.totalCommission, decimalPlaces))
                        StatRow(strings.resultTotalCost, Formatters.money(result.totalCost, decimalPlaces))
                        StatRow(strings.resultMargin, Formatters.percent(result.marginPercent))
                        StatRow(strings.resultRoi, Formatters.percent(result.roiPercent))
                    }
                }
            } else {
                item {
                    Text(
                        strings.validationRequired,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = input.productName,
                    onValueChange = viewModel::setProductName,
                    label = { Text(strings.inputProductName) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                SectionCard(strings.sectionResults) {
                    NumberField(
                        strings.inputPurchasePrice, input.purchasePrice, viewModel::setPurchasePrice,
                        isError = issueFor(ValidationField.PURCHASE_PRICE)?.severity == ValidationSeverity.ERROR,
                        supportingText = issueFor(ValidationField.PURCHASE_PRICE)?.let { strings.validationNegative },
                        resetSignal = formVersion,
                    )
                    NumberField(
                        strings.inputSalePrice, input.salePrice, viewModel::setSalePrice,
                        isError = issueFor(ValidationField.SALE_PRICE)?.severity == ValidationSeverity.ERROR,
                        supportingText = issueFor(ValidationField.SALE_PRICE)?.let { strings.validationNegative },
                        resetSignal = formVersion,
                    )
                    IntField(
                        strings.inputQuantity, input.quantity, viewModel::setQuantity,
                        isError = issueFor(ValidationField.QUANTITY)?.severity == ValidationSeverity.ERROR,
                        supportingText = issueFor(ValidationField.QUANTITY)?.let { strings.validationQuantityMin },
                        resetSignal = formVersion,
                    )
                    NumberField(
                        strings.inputCommission, input.commissionPercent, viewModel::setCommissionPercent,
                        suffix = "%",
                        isError = issueFor(ValidationField.COMMISSION)?.severity == ValidationSeverity.ERROR,
                        supportingText = issueFor(ValidationField.COMMISSION)?.let { strings.validationCommissionRange },
                        resetSignal = formVersion,
                    )
                    NumberField(strings.inputLogistics, input.logistics, viewModel::setLogistics, resetSignal = formVersion)
                    NumberField(strings.inputPackaging, input.packaging, viewModel::setPackaging, resetSignal = formVersion)

                    AdvertisingModeToggle(
                        mode = input.advertisingMode,
                        onModeChange = viewModel::setAdvertisingMode,
                        totalLabel = strings.adModeTotal,
                        perItemLabel = strings.adModePerItem,
                    )
                    NumberField(
                        strings.inputAdvertising, input.advertisingValue, viewModel::setAdvertisingValue,
                        resetSignal = formVersion,
                    )

                    NumberField(
                        strings.inputDiscount, input.discountPercent, viewModel::setDiscountPercent,
                        suffix = "%",
                        isError = issueFor(ValidationField.DISCOUNT)?.severity == ValidationSeverity.ERROR,
                        supportingText = issueFor(ValidationField.DISCOUNT)?.let { strings.validationDiscountRange },
                        resetSignal = formVersion,
                    )
                    NumberField(
                        strings.inputTax, input.taxPercent, viewModel::setTaxPercent, suffix = "%",
                        resetSignal = formVersion,
                    )
                    NumberField(
                        strings.inputOtherCosts, input.otherCosts, viewModel::setOtherCosts,
                        resetSignal = formVersion,
                    )
                }
            }

            if (result != null) {
                item {
                    Button(onClick = { viewModel.saveToHistory(input) }, modifier = Modifier.fillMaxWidth()) {
                        Text(strings.actionSave + " — " + strings.navHistory)
                    }
                }
                item {
                    OutlinedButton(onClick = { viewModel.saveAsProduct(input) }, modifier = Modifier.fillMaxWidth()) {
                        Text(strings.savedProductsSave + " — " + strings.navSavedProducts)
                    }
                }
                item {
                    SectionCard(strings.dashboardQuickActions) {
                        OutlinedButton(onClick = onOpenBreakEven, modifier = Modifier.fillMaxWidth()) {
                            Text(strings.navBreakEven)
                        }
                        OutlinedButton(onClick = onOpenTargetProfit, modifier = Modifier.fillMaxWidth()) {
                            Text(strings.targetProfitTitle)
                        }
                        OutlinedButton(onClick = onOpenMaxDiscount, modifier = Modifier.fillMaxWidth()) {
                            Text(strings.navMaxDiscount)
                        }
                    }
                }
                item {
                    OutlinedButton(
                        onClick = {
                            val file = CsvExporter.exportSingleResultToTxt(
                                context, input.productName.ifBlank { strings.navCalculator },
                                listOf(
                                    strings.resultProfitPerItem to Formatters.money(result.profitPerItem, decimalPlaces),
                                    strings.resultTotalProfit to Formatters.money(result.totalProfit, decimalPlaces),
                                    strings.resultMargin to Formatters.percent(result.marginPercent),
                                    strings.resultRoi to Formatters.percent(result.roiPercent),
                                ),
                            )
                            context.startActivity(
                                android.content.Intent.createChooser(
                                    CsvExporter.shareIntentFor(context, file), strings.shareResult,
                                ),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(strings.shareResult)
                    }
                }
            }
        }
    }
}
