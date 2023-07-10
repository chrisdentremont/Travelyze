package com.example.travelapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp.ui.theme.Aero

@Composable
fun Home(){
    Column(Modifier.fillMaxWidth()){
        Row(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .padding(30.dp)
                    .width(500.dp),
                fontSize = 60.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                text = "TRAVELYZE")
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            val focusManager = LocalFocusManager.current

            //TODO Create next page of home page once a search is conducted
            TextField(
                value = text,
                onValueChange = {text = it /*TODO Limit Characters*/},
                modifier = Modifier
                    .heightIn(0.dp, 50.dp)
                    .widthIn(0.dp, 280.dp)
                    .border(width = 1.dp, color = Color.Black),
                placeholder = { Text("Find Somewhere new!") },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                trailingIcon = {
                    Icon(imageVector = Icons.Outlined.Search,
                        contentDescription = "",
                        modifier = Modifier.size(size = 40.dp))
                }
            )
        }

        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { /*TODO*/},
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp)
                    .size(width = 150.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Aero)
            ) {
                Text(text = "Search", color = Color.White, fontSize = 20.sp)
            }
        }

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Recommended Places: ",
                fontSize = 30.sp,
                modifier = Modifier.padding(start =30.dp, top = 80.dp))
        }

        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(all = 20.dp)
                .verticalScroll(rememberScrollState())) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    elevation = 10.dp,
                ){
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Text(text = "Paris, France")
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    elevation = 10.dp,
                ){
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Text(text = "Las Vegas, California")
                        }
                    }
                }
            }
        }
    }
}
