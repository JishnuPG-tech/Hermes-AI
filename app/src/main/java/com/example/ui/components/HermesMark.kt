package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.HermesCoral
import com.example.ui.theme.HermesCoralLight
import kotlin.math.cos
import kotlin.math.sin

enum class HermesMarkState {
    IDLE,
    THINKING,
    LISTENING,
    SPEAKING,
    ACTING
}

@Composable
fun HermesMark(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    state: HermesMarkState = HermesMarkState.IDLE,
    coralColor: Color = HermesCoral
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hermes_mark")
    
    // Continuous rotation for thinking, speaking, and acting (signature Claude sunburst spin)
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (state) {
                    HermesMarkState.THINKING -> 6000
                    HermesMarkState.SPEAKING -> 4500
                    HermesMarkState.ACTING -> 3500
                    HermesMarkState.LISTENING -> 9000
                    HermesMarkState.IDLE -> 24000
                },
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    // Breathing pulse scale
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = when (state) {
            HermesMarkState.SPEAKING -> 1.18f
            HermesMarkState.THINKING -> 1.12f
            HermesMarkState.LISTENING -> 1.08f
            HermesMarkState.ACTING -> 1.14f
            HermesMarkState.IDLE -> 1.02f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (state) {
                    HermesMarkState.SPEAKING -> 500
                    HermesMarkState.THINKING -> 900
                    HermesMarkState.LISTENING -> 1200
                    HermesMarkState.ACTING -> 700
                    HermesMarkState.IDLE -> 2000
                },
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Glow intensity
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = if (state != HermesMarkState.IDLE) 0.55f else 0.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (state != HermesMarkState.IDLE) 800 else 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    // Secondary ripple wave for voice states (listening & speaking)
    val rippleProgress by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleProgress"
    )

    val rippleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rippleAlpha"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val baseRadius = this.size.minDimension / 2f
            val radius = baseRadius * pulseScale

            // Outward ripple ring during voice interactions
            if (state == HermesMarkState.LISTENING || state == HermesMarkState.SPEAKING) {
                drawCircle(
                    color = coralColor.copy(alpha = rippleAlpha),
                    radius = baseRadius * rippleProgress,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                )
            }

            // Ambient background radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        coralColor.copy(alpha = glowAlpha),
                        coralColor.copy(alpha = glowAlpha * 0.4f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.6f
                ),
                radius = radius * 1.6f,
                center = center
            )

            // Radiating sunburst rays with rotation
            rotate(degrees = rotationAngle, pivot = center) {
                // Central core dot
                val coreRadius = radius * 0.18f
                drawCircle(
                    color = HermesCoralLight,
                    radius = coreRadius,
                    center = center
                )

                // 12 radiating tapered rays
                val rayCount = 12
                val innerRadius = radius * 0.28f
                val outerRadius = radius * 0.88f
                val strokeWidth = radius * 0.12f

                for (i in 0 until rayCount) {
                    val angle = (i * (360f / rayCount)) * (Math.PI / 180f).toFloat()
                    val start = Offset(
                        x = center.x + innerRadius * cos(angle),
                        y = center.y + innerRadius * sin(angle)
                    )
                    val end = Offset(
                        x = center.x + outerRadius * cos(angle),
                        y = center.y + outerRadius * sin(angle)
                    )
                    drawLine(
                        color = coralColor,
                        start = start,
                        end = end,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

