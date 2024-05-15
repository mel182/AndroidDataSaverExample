@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.jetpackcomposesharedelementtransitionexample

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SharedTransitionScope.ListScreen(
    items: List<AnimationListItem>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onItemClick: (AnimationListItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach {
            ListItem(it, animatedVisibilityScope, onItemClick)
        }
    }
}

@Composable
private fun SharedTransitionScope.ListItem(
    item: AnimationListItem,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onItemClick: (AnimationListItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick(item)
            }
            .padding(all = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Image(
            painter = painterResource(id = item.image),
            modifier = Modifier
                .size(width = 150.dp, height = 75.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "image/${item.image}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }),
            contentDescription = null
        )

        Text(text = item.title, modifier = Modifier.sharedElement(
            state = rememberSharedContentState(key = "text/${item.title}"),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ ->
                tween(durationMillis = 1000)
            }), color = Color.Black
        )
    }
}