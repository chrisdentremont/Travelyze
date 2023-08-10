package com.travelapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.halcomFamily
import com.travelapp.ui.theme.marsFamily

var exploreLocationSelected = mutableStateOf(false)
var selectedName = mutableStateOf("")

/**
 * Displays the main search page and its contents.
 *
 * @param auth The [FirebaseAuth] instance used to get information about the current user state
 * @param nav The [NavController] used to navigate to other pages
 */
@Composable
fun Explore(auth: FirebaseAuth, nav: NavController) {

    Column(
        Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        Column(){
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp, 150.dp)
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .width(200.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontFamily = marsFamily,
                    text = "Travelyze"
                )
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
            var dropDownExpanded by remember { mutableStateOf(false) }
            var searchedLocations by remember { mutableStateOf(listOf<String>()) }

            fun onValueChanged(value: TextFieldValue) {
                if (!exploreLocationSelected.value) {
                    dropDownExpanded = value.text.isNotEmpty()
                    textFieldValue = value
                    searchedLocations = locationNames.filter {
                        it.lowercase()
                            .startsWith(value.text.lowercase()) && value.text.isNotEmpty() && it.lowercase() != value.text.lowercase()
                    }.take(3)
                }
            }

            Box(content = {
                OutlinedTextField(
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused)
                                dropDownExpanded = false
                        },
                    value = textFieldValue,
                    onValueChange = ::onValueChanged,
                    placeholder = { Text("Search for a place") },
                    colors = TextFieldDefaults.textFieldColors(
                        leadingIconColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        backgroundColor = Color.White,
                        cursorColor = Color.Black
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "",
                            modifier = Modifier.size(size = 25.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    maxLines = 1,
                )

                DropdownMenu(
                    offset = DpOffset(0.dp, 0.dp),
                    expanded = dropDownExpanded,
                    properties = PopupProperties(
                        focusable = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    ),
                    onDismissRequest = { dropDownExpanded = false }
                ) {
                    searchedLocations.forEach { text ->
                        DropdownMenuItem(onClick = {
                            onValueChanged(
                                TextFieldValue(
                                    text,
                                    TextRange(text.length)
                                )
                            )
                            selectedName.value = text
                            exploreLocationSelected.value = true
                            dropDownExpanded = false
                        }) {
                            Text(text = text)
                        }
                    }
                }
            })
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Recommended Places ",
                fontFamily = halcomFamily,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 80.dp)
            )
        }

        Row(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 60.dp)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable {
                            selectedName.value = "France"
                            exploreLocationSelected.value = true
                        },
                    elevation = 10.dp,
                ) {
                    Column() {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = "https://res.klook.com/image/upload/Mobile/City/swox6wjsl5ndvkv5jvum.jpg",
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        ) {
                            Text(text = "France")
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable {
                            selectedName.value = "United States"
                            exploreLocationSelected.value = true
                        },
                    elevation = 10.dp,
                ) {
                    Column() {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = "https://vegasexperience.com/wp-content/uploads/2023/01/Photo-of-Las-Vegas-Downtown-1920x1280.jpg",
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        ) {
                            Text(text = "United States")
                        }
                    }
                }
            }
        }
    }
}