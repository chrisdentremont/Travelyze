package com.travelapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.travelapp.composable.TravelyzeUser
import com.travelapp.ui.theme.Alabaster
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.TuftsBlue
import com.travelapp.ui.theme.*
import java.io.File

val openCancelRequestDialog = mutableStateOf(false)
val openAcceptRequestDialog = mutableStateOf(false)
val openRejectRequestDialog = mutableStateOf(false)
private val currentFriend = mutableStateOf("")

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendRequests() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Incoming", "Outgoing")

    if(openCancelRequestDialog.value){
        cancelFriendRequest()
    }

    if(openAcceptRequestDialog.value){
        acceptFriendRequest()
    }

    if(openRejectRequestDialog.value){
        rejectFriendRequest()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        Scaffold(
            topBar = {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            modifier = Modifier.background(color = Color.DarkGray),
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Filled.Inbox, "")
                                    1 -> Icon(Icons.Filled.ForwardToInbox, "")
                                }
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { openAddFriendDialog.value = true },
                    backgroundColor = TuftsBlue,
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )
            },
            content = {
                when (tabIndex) {
                    0 -> IncomingRequestsScreen()
                    1 -> OutgoingRequestsScreen()
                }
            }
        )
    }
}

@Composable
fun IncomingRequestsScreen() {
    val db = Firebase.firestore
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser.value.requests?.incomingFriendRequests.isNullOrEmpty()) {
            item {
                Text(
                    text = "Incoming Requests",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            for (incomingUser in currentUser.value.requests?.incomingFriendRequests!!) {
                val tempProfileImage = mutableStateOf<File>(File(""))
                val friendDocumentReference =
                    db.collection("users").document(incomingUser)
                var friend = mutableStateOf(TravelyzeUser(null, null, null))

                var profileImage =
                    Firebase.storage.reference.child("users/${incomingUser}/profile_picture.jpg")

                profileImage.getFile(profileImageFile.value).addOnCompleteListener {
                    tempProfileImage.value = profileImageFile.value
                }

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friend.value = documentSnapshot.toObject<TravelyzeUser>()!!
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Column(
                                    Modifier.fillMaxWidth(0.78f)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        //TODO Get individual pfps working
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                model =
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(tempProfileImage.value)
                                                    .size(Size.ORIGINAL)
                                                    .build()
                                            ),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(75.dp)
                                                .clip(CircleShape)
                                        )

                                        Text(
                                            "" + friend.value.info?.userName,
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }

                                Column(
                                    Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        IconButton(
                                            modifier = Modifier.padding(end = 15.dp),
                                            onClick = {
                                                openAcceptRequestDialog.value = true
                                                currentFriend.value = incomingUser
                                            },
                                        ) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = "",
                                                modifier = Modifier.size(size = 40.dp)
                                            )
                                        }
                                        IconButton(
                                            modifier = Modifier.padding(end = 10.dp),
                                            onClick = {
                                                openRejectRequestDialog.value = true
                                                currentFriend.value = incomingUser
                                            },
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "",
                                                modifier = Modifier.size(size = 40.dp)
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
}

@Composable
fun OutgoingRequestsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser.value.requests?.outgoingFriendRequests.isNullOrEmpty()) {
            item {
                Text(
                    text = "Outgoing Requests",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        } else {
            for (outgoingUser in currentUser.value.requests?.outgoingFriendRequests!!) {
                val tempProfileImage = mutableStateOf<File>(File(""))
                val db = Firebase.firestore
                val friendDocumentReference =
                    db.collection("users").document(outgoingUser)
                var friend = mutableStateOf(TravelyzeUser(null, null, null))

                var profileImage =
                    Firebase.storage.reference.child("users/${outgoingUser}/profile_picture.jpg")

                profileImage.getFile(profileImageFile.value).addOnCompleteListener {
                    tempProfileImage.value = profileImageFile.value
                }

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friend.value = documentSnapshot.toObject<TravelyzeUser>()!!
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Column(
                                    Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        //TODO Get individual pfps working
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                model =
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(tempProfileImage.value)
                                                    .size(Size.ORIGINAL)
                                                    .build()
                                            ),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(75.dp)
                                                .clip(CircleShape)
                                        )

                                        Text(
                                            "" + friend.value.info?.userName,
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }

                                Column(
                                    Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        IconButton(
                                            modifier = Modifier.padding(end = 9.dp),
                                            onClick = {
                                                openCancelRequestDialog.value = true
                                                currentFriend.value = outgoingUser
                                            },
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "",
                                                modifier = Modifier.size(size = 40.dp)
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
}

@Composable
fun acceptFriendRequest(){
    val contextForToast = LocalContext.current.applicationContext
    val db = Firebase.firestore

    AlertDialog(
        onDismissRequest = {
            openAcceptRequestDialog.value = false
        },
        title = {
            Text(
                text = "Friend Request",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "Do you wish to accept this friend request?",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val userID = Firebase.auth.currentUser?.uid.toString()
                val currUserDocumentReference =
                    db.collection("users").document(userID)


                val friendDocumentReference =
                    db.collection("users").document(currentFriend.value)
                var friendAccount = mutableStateOf(TravelyzeUser(null, null, null))

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friendAccount.value = documentSnapshot.toObject<TravelyzeUser>()!!


                    if (currentUser.value.requests?.incomingFriendRequests?.contains(
                            currentFriend.value) == true &&
                        friendAccount.value.requests?.outgoingFriendRequests?.contains(
                            userID) == true
                    ) {
                        //Add friend to friends list
                        currentUser.value.requests?.incomingFriendRequests?.remove(currentFriend.value)
                        currentUser.value.data?.friendsList?.add( currentFriend.value )

                        friendAccount.value.requests?.outgoingFriendRequests?.remove(userID)
                        friendAccount.value.data?.friendsList?.add( userID )

                        //Update accounts
                        currUserDocumentReference.set(currentUser.value)
                        friendDocumentReference.set(friendAccount.value)

                        openAcceptRequestDialog.value = false
                        Toast.makeText(
                            contextForToast,
                            "Friend request accepted, you are now friends!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }) {
                Text(
                    text = "ACCEPT",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openAcceptRequestDialog.value = false
            }) {
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}

@Composable
fun cancelFriendRequest(){
    val contextForToast = LocalContext.current.applicationContext
    val db = Firebase.firestore

    AlertDialog(
        onDismissRequest = {
            openCancelRequestDialog.value = false
        },
        title = {
            Text(
                text = "Friend Request",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "Are you sure you want to cancel this friend request?",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val userID = Firebase.auth.currentUser?.uid.toString()
                val currUserDocumentReference =
                    db.collection("users").document(userID)


                val friendDocumentReference =
                    db.collection("users").document(currentFriend.value)
                var friendAccount = mutableStateOf(TravelyzeUser(null, null, null))

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friendAccount.value = documentSnapshot.toObject<TravelyzeUser>()!!


                    if (currentUser.value.requests?.outgoingFriendRequests?.contains(
                            currentFriend.value) == true &&
                        friendAccount.value.requests?.incomingFriendRequests?.contains(
                            userID) == true
                    ) {
                        //Remove request from users respective lists
                        currentUser.value.requests?.outgoingFriendRequests?.remove(currentFriend.value)

                        friendAccount.value.requests?.incomingFriendRequests?.remove(userID)

                        //Update accounts
                        currUserDocumentReference.set(currentUser.value)
                        friendDocumentReference.set(friendAccount.value)

                        openCancelRequestDialog.value = false
                        Toast.makeText(
                            contextForToast,
                            "Friend request cancelled.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }) {
                Text(
                    text = "REMOVE",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openCancelRequestDialog.value = false
            }) {
                Text(
                    text = "KEEP",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}

@Composable
fun rejectFriendRequest(){
    val contextForToast = LocalContext.current.applicationContext
    val db = Firebase.firestore

    AlertDialog(
        onDismissRequest = {
            openCancelRequestDialog.value = false
        },
        title = {
            Text(
                text = "Friend Request",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "Are you sure you want to reject this friend request?",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        confirmButton = {
            TextButton(onClick = {
                //TODO Remove friend request from both users lists
                val userID = Firebase.auth.currentUser?.uid.toString()
                val currUserDocumentReference =
                    db.collection("users").document(userID)


                val friendDocumentReference =
                    db.collection("users").document(currentFriend.value)
                var friendAccount = mutableStateOf(TravelyzeUser(null, null, null))

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friendAccount.value = documentSnapshot.toObject<TravelyzeUser>()!!


                    if (currentUser.value.requests?.incomingFriendRequests?.contains(
                            currentFriend.value) == true &&
                        friendAccount.value.requests?.outgoingFriendRequests?.contains(
                            userID) == true
                    ) {
                        //Add friend to friends list
                        currentUser.value.requests?.incomingFriendRequests?.remove(currentFriend.value)

                        friendAccount.value.requests?.outgoingFriendRequests?.remove(userID)

                        //Update accounts
                        currUserDocumentReference.set(currentUser.value)
                        friendDocumentReference.set(friendAccount.value)

                        openRejectRequestDialog.value = false
                        Toast.makeText(
                            contextForToast,
                            "Friend request rejected.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }) {
                Text(
                    text = "REJECT",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openCancelRequestDialog.value = false
            }) {
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}
