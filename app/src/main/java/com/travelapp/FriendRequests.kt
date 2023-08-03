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
import com.travelapp.ui.theme.*
import java.io.File

//Variable responsible for opening the cancel friend request dialog
val openCancelRequestDialog = mutableStateOf(false)
//Variable responsible for opening the accept friend request dialog
val openAcceptRequestDialog = mutableStateOf(false)
//Variable responsible for opening the reject friend request dialog
val openRejectRequestDialog = mutableStateOf(false)
//Variable that saves the friend id for each request card (gets set when one of the cards are clicked)
private val currentFriendID = mutableStateOf("")

/**
 * The main method for creating the FriendRequests UI
 *
 * @suppress UnusedMaterialScaffoldPaddingParameter
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendRequests() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Incoming", "Outgoing")

    //Opens the cancelFriendRequest dialog
    if(openCancelRequestDialog.value){
        CancelFriendRequest()
    }

    //Opens the acceptFriendRequest dialog
    if(openAcceptRequestDialog.value){
        AcceptFriendRequest()
    }

    //Opens the rejectFriendRequest dialog
    if(openRejectRequestDialog.value){
        RejectFriendRequest()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        Scaffold(
            topBar = {
                Column(){
                    Row(){
                        //TopAppBar was used here instead of TopBar because this required two buttons on the bar instead of 1
                        TopAppBar(
                            title = {
                                Text(
                                    text = "",
                                    fontSize = 25.sp,
                                    fontFamily = robotoFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { isAddingFriend.value = false },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "",
                                        modifier = Modifier.size(size = 40.dp)
                                    )
                                }
                            },
                            backgroundColor = BackgroundAccentColor
                        )
                    }

                    Row(){
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
                    }
                }
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

/**
 * The method for creating the Incoming Friend Requests screen
 * This method creates and populates the incoming requests screen
 *
 * When switching onto the page it checks the user's incoming requests and
 * adds a card for each user in the requests list
 */
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
                Icon(
                    imageVector = Icons.Filled.Inbox,
                    contentDescription = "",
                    tint = Color.LightGray
                )
                Text(
                    text = "Incoming Requests",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.LightGray
                )
            }
        } else {
            for (incomingUser in currentUser.value.requests?.incomingFriendRequests!!) {
                val tempProfileImage = mutableStateOf<File>(File(""))
                val friendDocumentReference =
                    db.collection("users").document(incomingUser)
                val friend = mutableStateOf(TravelyzeUser(null, null, null))

                val profileImage =
                    Firebase.storage.reference.child("users/${incomingUser}/profile_picture.jpg")

                profileImage.getFile(profileImageFile.value).addOnCompleteListener {
                    tempProfileImage.value = profileImageFile.value
                }

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friend.value = documentSnapshot.toObject<TravelyzeUser>()!!
                }

                item {
                    Row() {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
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

                                Column() {
                                    Text(
                                        "${friend.value.info?.firstName} ${friend.value.info?.lastName}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                    )
                                    Text(
                                        "@${friend.value.info?.userName}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        color = Color.Gray,
                                    )
                                }

                                Column() {
                                    IconButton(
                                        onClick = {
                                            openAcceptRequestDialog.value = true
                                            currentFriendID.value = incomingUser
                                        },
                                    ) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "",
                                            modifier = Modifier.size(size = 40.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            openRejectRequestDialog.value = true
                                            currentFriendID.value = incomingUser
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

/**
 * The method for creating the Outgoing Friend Requests screen
 * This method creates and populates the outgoing requests screen
 *
 * When switching onto the page it checks the user's outgoing requests and
 * adds a card for each user in the requests list
 */
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
                Icon(
                    imageVector = Icons.Filled.ForwardToInbox,
                    contentDescription = "",
                    tint = Color.LightGray
                )

                Text(
                    text = "Outgoing Requests",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.LightGray
                )
            }

        } else {
            for (outgoingUser in currentUser.value.requests?.outgoingFriendRequests!!) {
                val tempProfileImage = mutableStateOf<File>(File(""))
                val db = Firebase.firestore
                val friendDocumentReference =
                    db.collection("users").document(outgoingUser)
                val friend = mutableStateOf(TravelyzeUser(null, null, null))

                val profileImage =
                    Firebase.storage.reference.child("users/${outgoingUser}/profile_picture.jpg")

                profileImage.getFile(profileImageFile.value).addOnCompleteListener {
                    tempProfileImage.value = profileImageFile.value
                }

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friend.value = documentSnapshot.toObject<TravelyzeUser>()!!
                }

                item {
                    Row() {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
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

                                Column() {
                                    Text(
                                        "${friend.value.info?.firstName} ${friend.value.info?.lastName}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                    )
                                    Text(
                                        "@${friend.value.info?.userName}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        color = Color.Gray,
                                    )
                                }

                                Column() {
                                    IconButton(
                                        onClick = {
                                            openRejectRequestDialog.value = true
                                            currentFriendID.value = outgoingUser
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

/**
 * This method is responsible for confirming the user wishes to accept an incoming friend request
 * and if so, adds that user to their friends list in the database
 */
@Composable
fun AcceptFriendRequest(){
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
                    db.collection("users").document(currentFriendID.value)
                val friendAccount = mutableStateOf(TravelyzeUser(null, null, null))

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friendAccount.value = documentSnapshot.toObject<TravelyzeUser>()!!


                    if (currentUser.value.requests?.incomingFriendRequests?.contains(
                            currentFriendID.value) == true &&
                        friendAccount.value.requests?.outgoingFriendRequests?.contains(
                            userID) == true
                    ) {
                        //Add friend to friends list
                        currentUser.value.requests?.incomingFriendRequests?.remove(currentFriendID.value)
                        currentUser.value.data?.friendsList?.add( currentFriendID.value )

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
                    color = TextButtonColor,
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

/**
 * This method is responsible for canceling an outgoing friend request if the user does not want to add another
 * specific user anymore, they can cancel the friend request and it is removed from both their outgoing requests list and the other user's
 * incoming requests list.
 */
@Composable
fun CancelFriendRequest(){
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
                    db.collection("users").document(currentFriendID.value)
                val friendAccount = mutableStateOf(TravelyzeUser(null, null, null))

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friendAccount.value = documentSnapshot.toObject<TravelyzeUser>()!!


                    if (currentUser.value.requests?.outgoingFriendRequests?.contains(
                            currentFriendID.value) == true &&
                        friendAccount.value.requests?.incomingFriendRequests?.contains(
                            userID) == true
                    ) {
                        //Remove request from users respective lists
                        currentUser.value.requests?.outgoingFriendRequests?.remove(currentFriendID.value)

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

/**
 * This method is responsible for rejecting an incoming friend request if the user does not want to add another
 * specific user, they can reject the friend request and it is removed from both their incoming requests list and the other user's
 * outgoing requests list.
 */
@Composable
fun RejectFriendRequest(){
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
                val userID = Firebase.auth.currentUser?.uid.toString()
                val currUserDocumentReference =
                    db.collection("users").document(userID)


                val friendDocumentReference =
                    db.collection("users").document(currentFriendID.value)
                val friendAccount = mutableStateOf(TravelyzeUser(null, null, null))

                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    friendAccount.value = documentSnapshot.toObject<TravelyzeUser>()!!


                    if (currentUser.value.requests?.incomingFriendRequests?.contains(
                            currentFriendID.value) == true &&
                        friendAccount.value.requests?.outgoingFriendRequests?.contains(
                            userID) == true
                    ) {
                        //Add friend to friends list
                        currentUser.value.requests?.incomingFriendRequests?.remove(currentFriendID.value)

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
