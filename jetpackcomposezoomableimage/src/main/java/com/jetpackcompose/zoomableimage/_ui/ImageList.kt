@file:OptIn(ExperimentalFoundationApi::class)

package com.jetpackcompose.zoomableimage._ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jetpackcompose.zoomableimage.data.Photo
import com.jetpackcompose.zoomableimage.domain.photoGridDragHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ImageList(
    modifier: Modifier = Modifier,
    photos: List<Photo>
) {
    var activeId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    val gridState = rememberLazyGridState()
    var autoScrollSpeed by remember { mutableFloatStateOf(0f) }
    val scrim = remember(activeId) {
        FocusRequester()
    }
    PhotoGrid(
        photos = photos,
        state = gridState,
        setAutoScrollSpeed = { autoScrollSpeed = it },
        navigateToPhoto = { activeId = it },
        modifier = modifier.focusProperties { canFocus = activeId == null }
    )

    if (activeId != null) {
        FullScreenPhoto(
            photo = photos.first { it.id == activeId },
            onDismiss = { activeId = null},
            modifier = Modifier.focusRequester(scrim)
        )

        LaunchedEffect(key1 = activeId) {
            scrim.requestFocus()
        }
    }

    LaunchedEffect(key1 = autoScrollSpeed) {
        if (autoScrollSpeed != 0f) {
            while (isActive) {
                gridState.scrollBy(autoScrollSpeed)
                delay(10.milliseconds)
            }
        }
    }
}

@Composable
private fun PhotoGrid(
    photos: List<Photo>,
    state: LazyGridState,
    modifier: Modifier = Modifier,
    setAutoScrollSpeed: (Float) -> Unit = { },
    navigateToPhoto: (Int) -> Unit = { }
) {
    var selectedIds by rememberSaveable { mutableStateOf(emptySet<Int>()) }
    val inSelectionMode by remember { derivedStateOf { selectedIds.isNotEmpty() } }

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Adaptive(128.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier.photoGridDragHandler(
            lazyGridState = state,
            selectedIds = { selectedIds },
            setSelectedIds = { selectedIds = it },
            setAutoScrollSpeed = setAutoScrollSpeed,
            autoScrollThreshold = with(LocalDensity.current) { 40.dp.toPx() }
        )
    ) {

        items(photos, key = { it.id }) { photo ->
            val selected by remember {
                derivedStateOf {
                    selectedIds.contains(photo.id)
                }
            }
            PhotoItem(
                photo = photo,
                inSelectedMode = inSelectionMode,
                selected = selected,
                modifier = Modifier
                    .semantics {
                        if (!inSelectionMode) {
                            onLongClick(label = "Select") {
                                selectedIds += photo.id
                                true
                            }
                        }
                    }
                    .then(
                        if (inSelectionMode) {
                            Modifier.toggleable(
                                value = selected,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onValueChange = {
                                    if (it) {
                                        selectedIds += photo.id
                                    } else {
                                        selectedIds -= photo.id
                                    }
                                }
                            )
                        } else {
                            Modifier.combinedClickable(
                                onClick = { navigateToPhoto(photo.id) },
                                onLongClick = { selectedIds += photo.id }
                            )
                        }
                    )
            )
        }
    }
}