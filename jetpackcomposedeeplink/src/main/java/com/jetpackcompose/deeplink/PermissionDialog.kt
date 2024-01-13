package com.jetpackcompose.deeplink

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(permissionTextProvider:PermissionTextProvider,
                     isPermanentlyDeclined:Boolean,
                     onDismiss: () -> Unit,
                     onOkClicked: () -> Unit,
                     onGoToAppSettingsClicked: () -> Unit,
                     modifier: Modifier = Modifier
) {

    AlertDialog(onDismissRequest = onDismiss,
        buttons = {

            Column(modifier = Modifier.fillMaxWidth()) {
                Divider()
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Grant permission"
                    } else {
                        "OK"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined)
                                onGoToAppSettingsClicked()
                            else
                                onOkClicked()
                        }
                        .padding(all = 16.dp)
                )
            }

        },
        title = {
            Text(text = "Permission required")
        },
        text = {

            Text(
                text = permissionTextProvider.getDescription(isPermanentlyDeclined = isPermanentlyDeclined)
            )
        },
        modifier = modifier
    )
}