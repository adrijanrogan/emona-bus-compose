package com.rogandev.lpp.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun BackTopAppBar(title: String, onBackClick: () -> Unit, actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(
        title = {
            Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                content = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            )
        }
    )
}
