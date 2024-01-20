import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCrescentMoon() {
    // This will generate values from 0f to 50f and back
    val jump by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    var lampOn by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(100.dp).offset(y = jump.dp)) {
            drawArc(
                color = if (lampOn) Color.Yellow else Color.Gray,
                startAngle = 45f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width / 4, size.height / 4),
                size = size / 2F
            )
        }
        Button(onClick = { lampOn = !lampOn }) {
            Text(if (lampOn) "Turn off" else "Turn on")
        }
    }
}

