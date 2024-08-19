package com.example.newsecidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsecidapp.ui.theme.NewSECIDappTheme
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewSECIDappTheme {
                CalculatorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Simple Calculator") })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = input,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = result,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                CalculatorButtons(
                    onButtonClick = { value ->
                        input = when (value) {
                            "=" -> {
                                try {
                                    val evalResult = evaluateExpression(input)
                                    result = evalResult.toString()
                                    ""
                                } catch (e: IllegalArgumentException) {
                                    result = "Error"
                                    ""
                                }
                            }
                            "C" -> {
                                result = ""
                                ""
                            }
                            else -> input + value
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun CalculatorButtons(onButtonClick: (String) -> Unit) {
    val buttons = listOf(
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "C", "0", "=", "+"
    )

    Column {
        buttons.chunked(4).forEach { row ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEach { button ->
                    CalculatorButton(button, onButtonClick)
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: (String) -> Unit) {
    Button(
        onClick = { onClick(text) },
        modifier = Modifier
            .padding(4.dp)
            .size(64.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

fun evaluateExpression(expression: String): Double {
    return try {
        val result = ExpressionBuilder(expression).build().evaluate()
        result
    } catch (e: Exception) {
        throw IllegalArgumentException("Invalid Expression")
    }
}
