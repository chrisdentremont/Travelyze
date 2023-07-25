package com.example.travelapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.travelapp.composable.CustomOutlinedTextField
import com.example.travelapp.composable.TopBar
import com.example.travelapp.composable.TravelyzeUser
import com.example.travelapp.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

val openAddFriendDialog = mutableStateOf(false)
@Composable
fun Social(){

    if(openAddFriendDialog.value){
        addFriendDialog()
    }
    
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = BackgroundColor)
    ){
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            TopBar(
                title = "Friends",
                buttonIcon = Icons.Outlined.Add,
                onButtonClicked = {
                    /*TODO Implement friend addition window*/
                    openAddFriendDialog.value = true
                }
            )
        }

        //TODO Improve the friend page UI and display information in a better way
        Row(){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/be718605-eac9-40a7-bccd-dea055256f78/d5d27jb-f8843464-3ce2-4419-be3d-4b1210ca52a4.jpg/v1/fill/w_877,h_620,q_75,strp/mudkip_by_star_soul_d5d27jb-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjIwIiwicGF0aCI6IlwvZlwvYmU3MTg2MDUtZWFjOS00MGE3LWJjY2QtZGVhMDU1MjU2Zjc4XC9kNWQyN2piLWY4ODQzNDY0LTNjZTItNDQxOS1iZTNkLTRiMTIxMGNhNTJhNC5qcGciLCJ3aWR0aCI6Ijw9ODc3In1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.6U--8zmMblPQSkaSs_gy1ATKl6euB83wHSn--e_36os"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .border(5.dp, Color.White, CircleShape)
                                    )

                                    Text(
                                        "Chris D'Entremont",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier
                                        .border(
                                            2.dp,
                                            SolidColor(Color.Black),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about Paris, France!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "Paris is a European city located in France...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter("https://www.theshirtlist.com/wp-content/uploads/2018/12/Rowlet.jpg"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .border(5.dp, Color.White, CircleShape)
                                    )
                                    Text(
                                        "Gabriel Madeira",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier
                                        .border(
                                            2.dp,
                                            SolidColor(Color.Black),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about London, England!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "London is a European city located in England...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter("https://media.istockphoto.com/id/458297017/photo/hello-kitty-pattern.jpg?s=612x612&w=0&k=20&c=IvVb5qyDu9_E_M0LCDPGFcpSIzr016ZHm9ZrXANQhQs="),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .border(5.dp, Color.White, CircleShape)
                                    )
                                    Text(
                                        "Mo Merchant",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier
                                        .border(
                                            2.dp,
                                            SolidColor(Color.Black),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about Las Vegas, Nevada!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "Las Vegas is an American city located in Nevada...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter("https://nintendosoup.com/wp-content/uploads/2022/03/Chloe_Eevee.jpg"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .border(5.dp, Color.White, CircleShape)
                                    )
                                    Text(
                                        "Ayla Hebert",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier
                                        .border(
                                            2.dp,
                                            SolidColor(Color.Black),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about São Paulo, Brazil!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "São Paulo is a South American city located in Brazil...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun addFriendDialog(){
    val contextForToast = LocalContext.current.applicationContext
    val focusManager = LocalFocusManager.current

    var username by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            openAddFriendDialog.value = false
        },
        title = {
            Text(
                text = "Search for a user:",
                fontFamily = robotoFamily,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        },
        text = {
            Column(){
                Row(){
                    Text(
                        text = "Search for a user:",
                        fontFamily = robotoFamily,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
                Row(){
                    CustomOutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Username",
                        leadingIconImageVector = Icons.Filled.Person2,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(username.isNotBlank()){
                        //TODO search for user with given username
                        //Send user a friend request
                    } else {
                        Toast.makeText(
                            contextForToast,
                            "Please enter a valid username.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ){
                Text(
                    text = "SEARCH",
                    color = TextButtonColor,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openAddFriendDialog.value = false
                }
            ){
                Text(
                    text = "CANCEL",
                    color = TextButtonColor,
                )
            }
        }
    )
}