package com.jetpackcompose.zoomableimage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ImageList(
    modifier: Modifier = Modifier,
    items: List<Photo>
) {

    val gridState = rememberLazyGridState()


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
        
    }


}