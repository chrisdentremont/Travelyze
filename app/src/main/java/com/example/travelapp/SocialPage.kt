package com.example.travelapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelapp.composable.CustomOutlinedTextField
import com.example.travelapp.composable.TopBar
import com.example.travelapp.ui.theme.Aero
import com.example.travelapp.ui.theme.Alabaster
import com.example.travelapp.ui.theme.SoftWhite

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
            .background(color = SoftWhite)
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
                            elevation = 10.dp,
                            backgroundColor = Alabaster
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
                            elevation = 10.dp,
                            backgroundColor = Alabaster
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
                            elevation = 10.dp,
                            backgroundColor = Alabaster
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
                            elevation = 10.dp,
                            backgroundColor = Alabaster
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

    Dialog(
        onDismissRequest = {
            openAddFriendDialog.value = false
        }
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp)),
            elevation = 4.dp
        ){
            Column(
                verticalArrangement = Arrangement.Center,
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White)
                        .border(2.dp, SolidColor(Color.Black), shape = RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center,
                ){
                    Text(
                        modifier = Modifier.padding(bottom = 140.dp, start = 10.dp, end = 10.dp),
                        text = "Enter your friend's username below:",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 18.sp,
                    )

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
                            .fillMaxWidth(0.8f)
                            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, top = 150.dp, end = 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Button(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(end = 10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Aero),
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
                            }) {
                            Text(
                                text = "Confirm",
                                color = Color.White,
                            )
                        }

                        Button(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(start = 10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Aero),
                            onClick = {
                                openAddFriendDialog.value = false
                            }) {
                            Text(
                                text = "Cancel",
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}