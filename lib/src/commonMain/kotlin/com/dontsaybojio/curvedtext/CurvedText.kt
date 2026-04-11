package com.dontsaybojio.curvedtext

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * A Composable that displays text curved along a circular arc, like a rainbow.
 *
 * @param text The text to display along the curve
 * @param radius The radius of the curve. Positive values curve upward (smile), 
 *               negative values curve downward (frown). Larger absolute values create gentler curves.
 * @param modifier Modifier to be applied to the component
 * @param style The text style to apply
 * @param color The color of the text. If [Color.Companion.Unspecified], uses the color from [style]
 * @param fontSize The size of the text
 * @param fontStyle The typeface variant to use
 * @param fontWeight The typeface thickness to use
 * @param fontFamily The font family to use
 * @param letterSpacing The amount of space to add between each letter
 * @param textDecoration The decorations to paint on the text
 *
 * Example usage:
 * ```
 * // Upward curve (rainbow-like)
 * CurvedText(
 *     text = "Hello Rainbow Text!",
 *     radius = 200.dp,
 *     style = MaterialTheme.typography.headlineMedium
 * )
 *
 * // Downward curve
 * CurvedText(
 *     text = "Curved Downward",
 *     radius = (-150).dp,
 *     color = Color.Blue
 * )
 * ```
 */
@Composable
fun CurvedText(
    text: String,
    radius: Dp,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current

    // Merge the provided style with individual parameters
    val mergedStyle = style.merge(
        TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration
        )
    )

    // Calculate character measurements and positions
    val curveData = remember(text, radius, mergedStyle, density) {
        calculateCurveData(text, radius, mergedStyle, textMeasurer, density)
    }

    Canvas(
        modifier = modifier.size(
            width = curveData.canvasWidth,
            height = curveData.canvasHeight
        )
    ) {
        drawCurvedText(
            text = text,
            curveData = curveData,
            textMeasurer = textMeasurer,
            style = mergedStyle
        )
    }
}

/**
 * Data class containing all the calculated information needed to draw curved text
 */
private data class CurveData(
    val radiusPx: Float,
    val centerX: Float,
    val centerY: Float,
    val canvasWidth: Dp,
    val canvasHeight: Dp,
    val characterPositions: List<CharacterPosition>
)

/**
 * Data class representing the position and rotation of a single character
 */
private data class CharacterPosition(
    val char: Char,
    val x: Float,
    val y: Float,
    val rotation: Float,
    val width: Float,
    val height: Float
)

/**
 * Data class representing the bounding box of a rotated rectangle
 */
private data class RotatedBounds(
    val minX: Float,
    val maxX: Float,
    val minY: Float,
    val maxY: Float
)

/**
 * Calculates the bounding box of a rotated rectangle
 */
private fun calculateRotatedBounds(
    centerX: Float,
    centerY: Float,
    width: Float,
    height: Float,
    rotationDegrees: Float
): RotatedBounds {
    val rotationRadians = rotationDegrees.toDouble() * (PI / 180.0)
    val cos = cos(rotationRadians).toFloat()
    val sin = sin(rotationRadians).toFloat()

    // Calculate the four corners of the rectangle relative to center
    val halfWidth = width / 2f
    val halfHeight = height / 2f

    // Original corners (relative to center)
    val corners = arrayOf(
        Pair(-halfWidth, -halfHeight), // Top-left
        Pair(halfWidth, -halfHeight),  // Top-right
        Pair(halfWidth, halfHeight),   // Bottom-right
        Pair(-halfWidth, halfHeight)   // Bottom-left
    )

    // Rotate each corner and find min/max bounds
    var minX = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var minY = Float.MAX_VALUE
    var maxY = Float.MIN_VALUE

    corners.forEach { (x, y) ->
        // Apply rotation transformation
        val rotatedX = centerX + (x * cos - y * sin)
        val rotatedY = centerY + (x * sin + y * cos)

        minX = minOf(minX, rotatedX)
        maxX = maxOf(maxX, rotatedX)
        minY = minOf(minY, rotatedY)
        maxY = maxOf(maxY, rotatedY)
    }

    return RotatedBounds(minX, maxX, minY, maxY)
}

/**
 * Calculates all the positioning data needed to draw the curved text
 */
private fun calculateCurveData(
    text: String,
    radius: Dp,
    style: TextStyle,
    textMeasurer: TextMeasurer,
    density: Density
): CurveData {
    with(density) {
        val radiusPx = radius.toPx()
        val absRadiusPx = abs(radiusPx)

        // Measure each character individually with full text metrics
        val characterMeasurements = text.map { char ->
            textMeasurer.measure(char.toString(), style)
        }

        val characterWidths = characterMeasurements.map { it.size.width.toFloat() }
        val textHeight = characterMeasurements.maxOfOrNull { it.size.height.toFloat() } ?: 0f

        // Calculate consistent inter-character spacing
        val averageCharWidth = if (characterWidths.isNotEmpty()) characterWidths.average().toFloat() else 0f
        val letterSpacing = averageCharWidth * 0.1f // 10% of average character width for spacing

        // Calculate total arc length including character widths and spacing
        val totalTextWidth = characterWidths.sum()
        val totalSpacing = if (text.length > 1) letterSpacing * (text.length - 1) else 0f
        val totalArcLength = totalTextWidth + totalSpacing
        val arcAngle = totalArcLength / absRadiusPx // Total angle in radians

        // Calculate character positions with proper spacing
        val characterPositions = mutableListOf<CharacterPosition>()
        var currentArcPosition = 0f // Current position along the arc length

        text.forEachIndexed { index, char ->
            val charWidth = characterWidths[index]
            val charHeight = characterMeasurements[index].size.height.toFloat()

            // Position character at its center point along the arc
            val charCenterArcPosition = currentArcPosition + charWidth / 2f
            val charCenterAngle = (charCenterArcPosition / totalArcLength) * arcAngle - arcAngle / 2f

            // Calculate position on the circle
            val x = absRadiusPx * sin(charCenterAngle)
            val y = if (radiusPx > 0) {
                // Upward curve (smile)
                absRadiusPx * (1 - cos(charCenterAngle))
            } else {
                // Downward curve (frown)
                -absRadiusPx * (1 - cos(charCenterAngle))
            }

            // Calculate rotation angle (tangent to the circle)
            val rotation = if (radiusPx > 0) {
                (charCenterAngle.toDouble() * (180.0 / PI)).toFloat()
            } else {
                (-charCenterAngle.toDouble() * (180.0 / PI)).toFloat()
            }

            characterPositions.add(
                CharacterPosition(
                    char = char,
                    x = x,
                    y = y,
                    rotation = rotation,
                    width = charWidth,
                    height = charHeight
                )
            )

            // Move to next character position (character width + spacing)
            currentArcPosition += charWidth + letterSpacing
        }

        // Calculate rotated bounds for each character
        val rotatedBounds = characterPositions.map { pos ->
            calculateRotatedBounds(pos.x, pos.y, pos.width, pos.height, pos.rotation)
        }

        // Find overall bounds including all rotated characters
        val minX = rotatedBounds.minOfOrNull { it.minX } ?: 0f
        val maxX = rotatedBounds.maxOfOrNull { it.maxX } ?: 0f
        val minY = rotatedBounds.minOfOrNull { it.minY } ?: 0f
        val maxY = rotatedBounds.maxOfOrNull { it.maxY } ?: 0f

        // Add dynamic padding based on text size
        val padding = maxOf(textHeight * 0.5f, 30f)
        val canvasWidth = (maxX - minX + padding * 2).toDp()
        val canvasHeight = (maxY - minY + padding * 2).toDp()

        // Adjust positions to center in canvas
        val offsetX = -minX + padding
        val offsetY = -minY + padding

        val adjustedPositions = characterPositions.map { pos ->
            pos.copy(
                x = pos.x + offsetX,
                y = pos.y + offsetY
            )
        }

        return CurveData(
            radiusPx = radiusPx,
            centerX = offsetX,
            centerY = if (radiusPx > 0) offsetY + absRadiusPx else offsetY - absRadiusPx,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,
            characterPositions = adjustedPositions
        )
    }
}

/**
 * Draws the curved text on the canvas
 */
private fun DrawScope.drawCurvedText(
    text: String,
    curveData: CurveData,
    textMeasurer: TextMeasurer,
    style: TextStyle
) {
    curveData.characterPositions.forEach { charPos ->
        rotate(
            degrees = charPos.rotation,
            pivot = Offset(charPos.x, charPos.y)
        ) {
            drawText(
                textMeasurer = textMeasurer,
                text = charPos.char.toString(),
                style = style,
                topLeft = Offset(
                    x = charPos.x - charPos.width / 2,
                    y = charPos.y - charPos.height / 2
                )
            )
        }
    }
}
