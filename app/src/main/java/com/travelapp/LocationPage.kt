package com.travelapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.robotoFamily

@Composable
fun LocationPage(
    name: String,
    nav: NavController
) {

    var selectedLocation = locationList.find { it.Name.equals(name) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            TopAppBar(
                title = {
                    Text(
                        text = "",
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            locationSelected.value = false
                            nav.navigate("explore")
                        },
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier.size(size = 40.dp),
                            tint = Color.Black
                        )
                    }
                },
                backgroundColor = BackgroundColor,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedLocation?.Name!!,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}