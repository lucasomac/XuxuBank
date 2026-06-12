package br.com.lucolimac.xuxubank.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp

@Composable
fun XuxuLogo(
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary
) {
    Canvas(modifier = modifier.size(120.dp)) {
        val width = size.width
        val height = size.height

        // Hat body (Simplified Cangaceiro Hat)
        val hatPath = Path().apply {
            moveTo(width * 0.2f, height * 0.5f)
            cubicTo(width * 0.2f, height * 0.3f, width * 0.8f, height * 0.3f, width * 0.8f, height * 0.5f)
            close()
        }
        drawPath(hatPath, color = Color(0xFFB8860B), style = Fill) // Dry Earth

        // Hat brim
        val brimPath = Path().apply {
            moveTo(width * 0.15f, height * 0.5f)
            lineTo(width * 0.85f, height * 0.5f)
            cubicTo(width * 0.85f, height * 0.6f, width * 0.15f, height * 0.6f, width * 0.15f, height * 0.5f)
            close()
        }
        drawPath(brimPath, color = Color(0xFF8D6E63), style = Fill)

        // Star
        val starPath = Path().apply {
            moveTo(width * 0.5f, height * 0.35f)
            lineTo(width * 0.52f, height * 0.4f)
            lineTo(width * 0.58f, height * 0.4f)
            lineTo(width * 0.53f, height * 0.43f)
            lineTo(width * 0.55f, height * 0.48f)
            lineTo(width * 0.5f, height * 0.45f)
            lineTo(width * 0.45f, height * 0.48f)
            lineTo(width * 0.47f, height * 0.43f)
            lineTo(width * 0.42f, height * 0.4f)
            lineTo(width * 0.48f, height * 0.4f)
            close()
        }
        drawPath(starPath, color = Color(0xFFFDB813), style = Fill) // Sun Yellow
    }
}
