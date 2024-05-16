package com.jetpackcompose.zoomableimage

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect

fun Modifier.photoGridDragHandler(
    lazyGridState: LazyGridState,
    selectedIds: () -> Set<Int>,
    autoScrollThreshold: Float,
    setSelectedIds: (Set<Int>) -> Unit = {},
    setAutoScrollSpeed: (Float) -> Unit = {}
) = this.pointerInput(autoScrollThreshold, setSelectedIds, setAutoScrollSpeed) {
    fun photoIdAtOffset(hitPoint: Offset): Int? = lazyGridState.layoutInfo.visibleItemsInfo.find { itemInfo ->
        itemInfo.size.toIntRect().contains(hitPoint.round() - itemInfo.offset)
    }?.key as? Int

    var initialPhotoId: Int? = null
    var currentPhotoId: Int? = null
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            photoIdAtOffset(offset)?.let { key ->
                if (!selectedIds().contains(key)) {
                    initialPhotoId = key
                    currentPhotoId = key
                    setSelectedIds(selectedIds() + key)
                }
            }
        },
        onDragCancel = {
            setAutoScrollSpeed(0f)
            initialPhotoId = null
        },
        onDragEnd = {
            setAutoScrollSpeed(0f)
            initialPhotoId = null
        },
        onDrag = { change, _ ->
            if (initialPhotoId != null) {
                val distanceFromBottom = lazyGridState.layoutInfo.viewportSize.height - change.position.y
                val distanceFromTop = change.position.y
                setAutoScrollSpeed(
                    when {
                        distanceFromBottom < autoScrollThreshold -> autoScrollThreshold - distanceFromBottom
                        distanceFromTop < autoScrollThreshold -> (autoScrollThreshold - distanceFromTop)
                        else -> 0f
                    }
                )

                photoIdAtOffset(change.position)?.let { pointerPhotoId ->
                    if (currentPhotoId != pointerPhotoId) {
                        setSelectedIds(
                            selectedIds().addOrRemoveUpTo(pointerPhotoId, currentPhotoId, initialPhotoId)
                        )
                        currentPhotoId = pointerPhotoId
                    }
                }
            }
        }
    )
}