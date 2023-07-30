package com.travelapp

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.travelapp.composable.TravelyzeUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.travelapp.composable.CustomOutlinedTextField
import com.travelapp.composable.TopBar
import com.travelapp.ui.theme.Alabaster
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.TextButtonColor
import com.travelapp.ui.theme.robotoFamily
import java.io.File

val openAddFriendDialog = mutableStateOf(false)
val isAddingFriend = mutableStateOf(false)
private val currentFriendPage = mutableStateOf("")

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Social() {
    //TODO Improve the friend page UI and display information in a better way
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = BackgroundColor)
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Friends",
                    buttonIcon = Icons.Filled.PersonAdd,
                    onButtonClicked = {
                        isAddingFriend.value = true
                    }
                )
            },
            content = {
                val db = Firebase.firestore
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (currentUser.value.data?.friendsList.isNullOrEmpty()) {
                        item {
                            Text(
                                text = "No friends yet, add some!",
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        for (addedFriend in currentUser.value.data?.friendsList!!) {
                            val tempProfileImage = mutableStateOf<File>(File.createTempFile("$addedFriend", ".jpg"))
                            val friendDocumentReference =
                                db.collection("users").document(addedFriend)
                            var friend = mutableStateOf(TravelyzeUser(null, null, null))
                            var displayedFriend = mutableStateOf<File>(File(""))

                            var profileImage =
                                Firebase.storage.reference.child("users/${addedFriend}/profile_picture.jpg")

                            profileImage.getFile(tempProfileImage.value).addOnCompleteListener {
                                displayedFriend.value = tempProfileImage.value
                            }

                            friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                                friend.value = documentSnapshot.toObject<TravelyzeUser>()!!
                            }

                            item {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 30.dp)
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { /*TODO Open Profile Page*/ },
                                        colors = CardDefaults.cardColors(Color.Transparent)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Image(
                                                painter = rememberAsyncImagePainter(
                                                    model =
                                                    ImageRequest.Builder(LocalContext.current)
                                                        .data(displayedFriend.value)
                                                        .size(Size.ORIGINAL)
                                                        .build()
                                                ),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(90.dp)
                                                    .clip(CircleShape)
                                            )

                                            Text(
                                                "" + friend.value.info?.userName,
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Normal,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(start = 15.dp)
                                            )
                                        }
                                    }
                                }

                                Divider(
                                    modifier = Modifier.padding(vertical = 40.dp, horizontal = 70.dp),
                                    color = Color.Gray,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun addFriendDialog() {
    val contextForToast = LocalContext.current.applicationContext
    val focusManager = LocalFocusManager.current
    val db = Firebase.firestore

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
                        if (currentUser.value.info?.userName == username) {
                            Toast.makeText(
                                contextForToast,
                                "You can't add yourself as a friend, sorry!.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            //Send user a friend request
                            val docRef = db.collection("users")
                                .whereEqualTo("info.userName", username)
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (documents.size() > 0) {
                                        val friend = documents.elementAt(0)
                                        var friendAccount = friend.toObject<TravelyzeUser>()

                                        val userID = Firebase.auth.currentUser?.uid.toString()
                                        val currUserDocumentReference =
                                            db.collection("users").document(userID)
                                        val friendDocumentReference =
                                            db.collection("users").document(friend.id)

                                        if (currentUser.value.requests?.outgoingFriendRequests?.contains(
                                                friend.id
                                            ) == false &&
                                            currentUser.value.requests?.incomingFriendRequests?.contains(
                                                friend.id
                                            ) == false
                                        ) {
                                            //Add card to requests page
                                            currentUser.value.requests?.outgoingFriendRequests?.add(
                                                friend.id
                                            )
                                            friendAccount?.requests?.incomingFriendRequests?.add(
                                                userID
                                            )

                                            currUserDocumentReference.set(currentUser.value)

                                            friendDocumentReference.set(friendAccount)

                                            openAddFriendDialog.value = false

                                            Toast.makeText(
                                                contextForToast,
                                                "Friend request sent!.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            openAddFriendDialog.value = false

                                            Toast.makeText(
                                                contextForToast,
                                                "Friend request already sent or received!.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

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

@Composable
fun displayFriendProfile(){
    //TODO Display profile
}

