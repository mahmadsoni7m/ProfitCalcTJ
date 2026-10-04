package com.profitcalc.tj.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.util.Formatters

private enum class Op { ADD, SUB, MUL, DIV, PERCENT }

private data class CalcKey(val label: String, val weight: Float = 1f)

private val CalcButtonShape = RoundedCornerShape(10.dp)
private val CalcButtonHeight = 50.dp
private val OperatorKeys = setOf("÷", "×", "−", "+", "=", "%", "⌫", "C")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickCalculatorScreen() {
    val strings = LocalAppStrings.current
    var display by remember { mutableStateOf("0") }
    var storedValue by remember { mutableStateOf<Double?>(null) }
    var pendingOp by remember { mutableStateOf<Op?>(null) }
    var justEvaluated by remember { mutableStateOf(false) }

    fun currentValue(): Double = Formatters.parseInput(display)

    fun applyOp(a: Double, b: Double, op: Op): Double = when (op) {
        Op.ADD -> a + b
        Op.SUB -> a - b
        Op.MUL -> a * b
        Op.DIV -> if (b == 0.0) 0.0 else a / b
        Op.PERCENT -> a * (b / 100.0)
    }

    fun onDigit(d: String) {
        if (justEvaluated) {
            display = ""
            justEvaluated = false
        }
        display = when {
            display == "0" && d != "." -> d
            d == "." && display.contains(".") -> display
            else -> display + d
        }
    }

    fun onBackspace() {
        justEvaluated = false
        display = if (display.length <= 1) "0" else display.dropLast(1)
    }

    fun onOp(op: Op) {
        val prev = storedValue
        if (prev != null && pendingOp != null) {
            val result = applyOp(prev, currentValue(), pendingOp!!)
            display = Formatters.money(result).replace(",", "")
            storedValue = result
        } else {
            storedValue = currentValue()
        }
        pendingOp = op
        justEvaluated = true
    }

    fun onEquals() {
        val prev = storedValue
        val op = pendingOp
        if (prev != null && op != null) {
            val result = applyOp(prev, currentValue(), op)
            display = Formatters.money(result).replace(",", "")
        }
        storedValue = null
        pendingOp = null
        justEvaluated = true
    }

    fun onClear() {
        display = "0"
        storedValue = null
        pendingOp = null
        justEvaluated = false
    }

    fun onKey(key: String) {
        when (key) {
            "C" -> onClear()
            "⌫" -> onBackspace()
            "÷" -> onOp(Op.DIV)
            "×" -> onOp(Op.MUL)
            "−" -> onOp(Op.SUB)
            "+" -> onOp(Op.ADD)
            "%" -> onOp(Op.PERCENT)
            "=" -> onEquals()
            else -> onDigit(key)
        }
    }

    val rows: List<List<CalcKey>> = listOf(
        listOf(CalcKey("C"), CalcKey("⌫"), CalcKey("%"), CalcKey("÷")),
        listOf(CalcKey("7"), CalcKey("8"), CalcKey("9"), CalcKey("×")),
        listOf(CalcKey("4"), CalcKey("5"), CalcKey("6"), CalcKey("−")),
        listOf(CalcKey("1"), CalcKey("2"), CalcKey("3"), CalcKey("+")),
        listOf(CalcKey("0", weight = 2f), CalcKey("."), CalcKey("=")),
    )

    Scaffold(topBar = { TopAppBar(title = { Text(strings.quickCalcTitle) }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Card(Modifier.fillMaxWidth()) {
                Text(
                    display,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 36.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.End,
                )
            }

            Spacer(Modifier.height(4.dp))

            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    row.forEach { calcKey ->
                        val isOperator = calcKey.label in OperatorKeys
                        Button(
                            onClick = { onKey(calcKey.label) },
                            modifier = Modifier.weight(calcKey.weight).height(CalcButtonHeight),
                            shape = CalcButtonShape,
                            colors = if (isOperator) {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                        ) {
                            Text(calcKey.label, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
