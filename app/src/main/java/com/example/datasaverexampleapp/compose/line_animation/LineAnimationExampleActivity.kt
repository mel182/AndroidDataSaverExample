@file:OptIn(ExperimentalAnimationApi::class)

package com.example.datasaverexampleapp.compose.line_animation

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.datasaverexampleapp.type_alias.Drawable
import kotlinx.coroutines.delay

class LineAnimationExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.apply {
            decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

            statusBarColor = android.graphics.Color.TRANSPARENT

            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        supportActionBar?.hide()

        setContent {

            var visible by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                AnimateLines {
                    visible = true
                }

                // Fade in
                AnimatedVisibility(visible = visible, enter = fadeIn(initialAlpha = 0f, animationSpec = tween(durationMillis = 800))) {
                    Image(painter = painterResource(id = Drawable.vi_icon), modifier = Modifier.size(100.dp),contentDescription = null)
                }

                // Scale in + expand vertically
//                AnimatedVisibility(visible = visible, enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically)) {
//                    Image(painter = painterResource(id = Drawable.vi_icon), modifier = Modifier.size(100.dp),contentDescription = null)
//                }
            }
        }
    }
}

@Composable
private fun AnimateLines(onCompletion:() -> Unit) {

    val lineColor = Color(0x1AFFFFFF)
    val animateValue = remember {
        Animatable(initialValue = 0f)
    }

    val density = LocalDensity.current.density
    val config = LocalConfiguration.current
    val screen_width = config.screenWidthDp.dp.value * density
    val screen_height = config.screenHeightDp.dp.value * density

    val guidelineWidth = screen_width
    val guidelineHeight = screen_height + 350f

    val targetHeight = screen_height + 350f

    val animatedDirectionGuide = animateValue.value * guidelineWidth

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFFFF9432))) {

        // Guide line
        drawLine(
            color = Color.Transparent,
            start = Offset(x = 0f, y = targetHeight),
            end = Offset(x = animatedDirectionGuide , y = guidelineHeight - (animateValue.value * guidelineHeight)),
            strokeWidth = 60f
        )

        // Line 1
        drawLine(
            color = lineColor,
            start = Offset(x = -69f, y = targetHeight),
            end = Offset(x = animatedDirectionGuide - 69f, y = targetHeight - (animateValue.value * targetHeight)),
            strokeWidth = 60f
        )

        // Line 2
        drawLine(
            color = lineColor,
            start = Offset(x = 69f, y = targetHeight),
            end = Offset(x = animatedDirectionGuide + 69f, y = targetHeight - (animateValue.value * targetHeight)),
            strokeWidth = 60f
        )
    }

    LaunchedEffect(key1 = true) {
        animateValue.animateTo(targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutLinearInEasing)
        )
        delay(500)
        onCompletion()
    }
}