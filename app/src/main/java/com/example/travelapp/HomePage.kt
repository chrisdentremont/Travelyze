package com.example.travelapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Home(words: String){
    Text(
        modifier = Modifier.padding(30.dp).width(500.dp),
        fontSize = 60.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        text = "$words")
}

@Preview
@Composable
fun testHome(){
    Home("TRAVELYZE")
}