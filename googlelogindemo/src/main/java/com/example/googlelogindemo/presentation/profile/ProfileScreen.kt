package com.example.googlelogindemo.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.googlelogindemo.presentation.sign_in.UserData

@Composable
fun ProfileScreen(userData: UserData?, onSignOut: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        userData?.run {

            profilePictureUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            userName?.let {
                Text(text = it,
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Button(onClick = { onSignOut() }) {
            Text(text = "Sign out")
        }

    }

}