package com.example.draganddropexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(this).apply {
                setContent {

                    LongPressedDraggable(modifier = Modifier.fillMaxSize().background(color = Color(0xFF6DC5FF))) {

                        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                            val (content,placeholder) = createRefs()

                            val dragInfo = LocalDragTargetInfo.current

                            DropTarget<String>(modifier = Modifier
                                .size(80.dp)
                                .constrainAs(placeholder) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top, 20.dp)
                                }) { isInBound, data ->

                                val backgroundColor = if (isInBound || dragInfo.dropBoundReached) { Color(0xFF5632E8) } else { Color(0x66000000) }
                                val borderColor = if (isInBound || dragInfo.dropBoundReached) { Color(0xFFACEC41) } else { Color.White }

                                Box(modifier = Modifier
                                    .size(80.dp)
                                    .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                                    .background(color = backgroundColor, shape = RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isInBound && dragInfo.dataToDrop.isNotBlank()){
                                        dragInfo.dropBoundReached = true
                                    }
                                }
                            }

                            DragTarget(modifier = Modifier
                                .size(80.dp)
                                .constrainAs(content) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }, dataToDrop = "Toppie")
                            {
                                Box(modifier = Modifier
                                    .size(80.dp)
                                    .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                                    .background(color = Color(0x66000000), shape = RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

@Composable
fun LongPressedDraggable(modifier: Modifier,
                         content: @Composable BoxScope.() -> Unit
) {
    val state = remember { DragTargetInfo() }
    CompositionLocalProvider(LocalDragTargetInfo provides state) {

        Box(modifier = modifier.fillMaxSize()) {
            content()
            if (state.isDragging) {

                var targetSize by remember {
                    mutableStateOf(IntSize.Zero)
                }

                Box(modifier = Modifier
                    .graphicsLayer {
                        val offset = (state.dragPosition + state.dragOffset)
                        scaleX = 1.3f
                        scaleY = 1.3f
                        alpha = if (targetSize == IntSize.Zero) 0f else .9f
                        translationX = offset.x.minus(targetSize.width / 2)
                        translationY = offset.y.minus(targetSize.height / 2)
                    }
                    .onGloballyPositioned {
                        targetSize = it.size
                    }
                ) {
                    state.draggableComposable?.invoke()
                }
            }
        }
    }
}

@Composable
fun <T>DragTarget(modifier: Modifier,
                  dataToDrop: T,
                  content: @Composable (() -> Unit)
) {

    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDragTargetInfo.current

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = {
                currentState.dataToDrop = "Toppie"
                currentState.isDragging = true
                currentState.dragPosition = currentPosition + it
                currentState.draggableComposable = content
            }, onDrag = { change, dragAmount ->
                change.consumeAllChanges()
                currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
            }, onDragEnd = {
                currentState.isDragging = false
                currentState.dragOffset = Offset.Zero
            }, onDragCancel = {
                currentState.dragOffset = Offset.Zero
                currentState.isDragging = false
            })
        }) {
        content()
    }
}

@Composable
fun <T> DropTarget(modifier: Modifier,
                   content: @Composable() (BoxScope.(isInBound: Boolean, data: T?) -> Unit)
) {
    val dragInfo = LocalDragTargetInfo.current
    val dragPosition = dragInfo.dragPosition
    val dragOffset = dragInfo.dragOffset
    var isCurrentDropTarget by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier.onGloballyPositioned {
        it.boundsInWindow().let { rect ->
            isCurrentDropTarget = rect.contains(dragPosition + dragOffset)
        }
    }) {
        val data = if (isCurrentDropTarget && !dragInfo.isDragging) dragInfo.dataToDrop as T? else null
        content(isCurrentDropTarget, data)
    }
}

internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<String>("")
    var dropBoundReached by mutableStateOf<Boolean>(false)
}