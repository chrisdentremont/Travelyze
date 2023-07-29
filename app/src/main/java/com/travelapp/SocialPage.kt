package com.travelapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.travelapp.composable.CustomOutlinedTextField
import com.travelapp.composable.TopBar
import com.travelapp.ui.theme.*

val openAddFriendDialog = mutableStateOf(false)
val isAddingFriend = mutableStateOf(false)

@Composable
fun Social() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = BackgroundColor)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TopBar(
                title = "Friends",
                buttonIcon = Icons.Filled.PersonAdd,
                onButtonClicked = {
                    isAddingFriend.value = true
                },
            )
        }

        //TODO Improve the friend page UI and display information in a better way
        Row() {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column() {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ) {
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
                                    ) {
                                        Text(
                                            text = "I read about Paris, France!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ) {
                                        Text(
                                            text = "Paris is a European city located in France...",
                                            modifier = Modifier.padding(
                                                start = 10.dp,
                                                bottom = 15.dp
                                            ),
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
fun addFriendDialog() {
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
                text = "Add a New Friend",
                fontFamily = robotoFamily,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        },
        text = {
            Column() {
                Row() {
                    Text(
                        text = "Enter another username exactly as it appears to send a friend request:",
                        fontFamily = robotoFamily,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
                Row() {
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
                    if (username.isNotBlank()) {
                        //Send user a friend request

                        val db = Firebase.firestore
                        val docRef = db.collection("users")
                            .whereEqualTo("info.userName", username)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.size() > 0) {
                                    val friend = documents.elementAt(0)
                                    //TODO Remove Logs
                                    Log.d(
                                        MainActivity.TAG,
                                        "User Search - friend ${friend.id} is ${friend.data}"
                                    )

                                    openAddFriendDialog.value = false
                                    Toast.makeText(
                                        contextForToast,
                                        "Friend request sent!.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Log.d(MainActivity.TAG, "User Search - No user found")

                                    Toast.makeText(
                                        contextForToast,
                                        "Please enter a valid username Make sure you enter it exactly!.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }.addOnFailureListener { exception ->
                                Log.w(
                                    MainActivity.TAG,
                                    "User Search - Error getting documents: ",
                                    exception
                                )
                            }

                    } else {
                        Toast.makeText(
                            contextForToast,
                            "Please enter a valid username.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
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
            ) {
                Text(
                    text = "CANCEL",
                    color = TextButtonColor,
                )
            }
        }
    )
}