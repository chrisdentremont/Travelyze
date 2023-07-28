package com.travelapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.travelapp.composable.TextFieldWithDropdown
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.marsFamily
import com.travelapp.ui.theme.robotoFamily
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(auth: FirebaseAuth){
    Column(
        Modifier
            .fillMaxWidth()
            .background(color = BackgroundColor)){
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 30.dp)
                    .width(200.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontFamily = marsFamily,
                text = "Travelyze")
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            var textFieldValue by remember {mutableStateOf(TextFieldValue())}
            var dropDownExpanded by remember {mutableStateOf(false)}
            var searchedLocations by remember {mutableStateOf(listOf<String>())}

            fun onValueChanged(value: TextFieldValue) {
                dropDownExpanded = value.text.isNotEmpty()
                textFieldValue = value
                searchedLocations = locationNames.filter {it.lowercase().startsWith(value.text.lowercase()) && value.text.isNotEmpty() && it.lowercase() != value.text.lowercase() }.take(3)
            }

            TextFieldWithDropdown(
                value = textFieldValue,
                setValue = ::onValueChanged,
                onDismissRequest = { dropDownExpanded = false },
                dropDownExpanded = dropDownExpanded,
                list = searchedLocations
            )

//            var text by rememberSaveable { mutableStateOf("") }
//            val focusManager = LocalFocusManager.current
//
//            //TODO Create next page of home page once a search is conducted
//            OutlinedTextField(
//                value = text,
//                onValueChange = {text = it /*TODO Limit Characters*/},
//                modifier = Modifier
//                    .heightIn(0.dp, 50.dp)
//                    .widthIn(0.dp, 280.dp),
//                placeholder = { Text("Search for a place") },
//                keyboardActions = KeyboardActions(onSearch = {
//                    /*TODO Search for locations*/
//                    focusManager.clearFocus()
//                }),
//                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search, keyboardType = KeyboardType.Text),
//                trailingIcon = {
//                    Icon(imageVector = Icons.Outlined.Search,
//                        contentDescription = "",
//                        modifier = Modifier.size(size = 25.dp))
//                },
//                colors = TextFieldDefaults.textFieldColors(
//                    leadingIconColor = Color.Black,
//                    focusedIndicatorColor = Color.Black,
//                    backgroundColor = Color.White,
//                    cursorColor = Color.Black
//                )
//            )
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Recommended Places ",
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Light,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 80.dp))
        }

        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 10.dp)
                .verticalScroll(rememberScrollState())) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    elevation = 10.dp,
                ){
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
                            Text(text = "Las Vegas, Nevada")
                        }
                    }
                }
            }
        }
    }
}