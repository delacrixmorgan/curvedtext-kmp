package com.dontsaybojio.curvedtext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dontsabojio.curvedtext.CurvedText

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "CurvedText",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            // Rainbow-like upward curves
            ExampleSection(
                title = "Upward Curves (Rainbow Style)",
                examples = listOf(
                    CurvedTextExample(
                        text = "Hello Rainbow Text!",
                        radius = 200.dp,
                        description = "Large radius (200dp) - gentle curve",
                        style = null,
                    ),
                    CurvedTextExample(
                        text = "CURVED HEADLINE",
                        radius = 150.dp,
                        description = "Medium radius (150dp) - moderate curve",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6200EE),
                        ),
                    ),
                    CurvedTextExample(
                        text = "Tight Curve",
                        radius = 80.dp,
                        description = "Small radius (80dp) - tight curve",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFF03DAC6),
                        ),
                    ),
                ),
            )

            // Downward curves
            ExampleSection(
                title = "Downward Curves (Frown Style)",
                examples = listOf(
                    CurvedTextExample(
                        text = "Downward Arc",
                        radius = (-200).dp,
                        description = "Large negative radius (-200dp)",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFFFF6B35),
                        ),
                    ),
                    CurvedTextExample(
                        text = "Steep Descent",
                        radius = (-100).dp,
                        description = "Medium negative radius (-100dp)",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE91E63),
                        ),
                    ),
                ),
            )

            // Different text lengths
            ExampleSection(
                title = "Various Text Lengths",
                examples = listOf(
                    CurvedTextExample(
                        text = "Short",
                        radius = 120.dp,
                        description = "Short text",
                        style = null,
                    ),
                    CurvedTextExample(
                        text = "This is a much longer text to demonstrate how the curve adapts",
                        radius = 180.dp,
                        description = "Long text with same radius",
                        style = null,
                    ),
                    CurvedTextExample(
                        text = "A",
                        radius = 100.dp,
                        description = "Single character",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9C27B0),
                        ),
                    ),
                ),
            )

            // Different font sizes
            ExampleSection(
                title = "Different Font Sizes",
                examples = listOf(
                    CurvedTextExample(
                        text = "Small Text",
                        radius = 150.dp,
                        description = "Small font size",
                        style = MaterialTheme.typography.bodySmall,
                    ),
                    CurvedTextExample(
                        text = "Large Text",
                        radius = 150.dp,
                        description = "Large font size",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    ),
                ),
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ExampleSection(
    title: String,
    examples: List<CurvedTextExample>
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
        examples.forEach { example ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = example.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small,
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CurvedText(
                            text = example.text,
                            radius = example.radius,
                            style = example.style ?: MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

private data class CurvedTextExample(
    val text: String,
    val radius: Dp,
    val description: String,
    val style: TextStyle? = null
)