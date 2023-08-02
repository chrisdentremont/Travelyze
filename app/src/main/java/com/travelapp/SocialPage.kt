package com.travelapp

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.graphics.FilterQuality
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
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
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
import com.travelapp.ui.theme.*
import java.io.File

val openAddFriendDialog = mutableStateOf(false)
val isAddingFriend = mutableStateOf(false)
val openFriendProfile = mutableStateOf(false)
val openRemoveFriendDialog = mutableStateOf(false)

private val currentFriendID = mutableStateOf("")
private val currentFriendPage = mutableStateOf(TravelyzeUser(null, null, null))
private val displayedFriendFile = mutableStateOf<File>(File(""))
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Social() {
    if(openFriendProfile.value){
        displayFriendProfile()
    }

    if(openRemoveFriendDialog.value){
        removeFriendDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = BackgroundColor)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        androidx.compose.material.Text(
                            text = "Friends",
                            fontSize = 25.sp,
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { openAddFriendDialog.value = true },
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Icon(
                                Icons.Filled.PersonAdd,
                                contentDescription = "",
                                modifier = Modifier.size(size = 40.dp)
                            )
                        }

                        IconButton(
                            onClick = { isAddingFriend.value = true },
                        ) {
                            Icon(
                                Icons.Filled.Inbox,
                                contentDescription = "",
                                modifier = Modifier.size(size = 40.dp)
                            )
                        }
                    },
                    backgroundColor = BackgroundAccentColor
                )
            },
            content = {
                val db = Firebase.firestore
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (currentUser.value.data?.friendsList.isNullOrEmpty()) {
                        item {
                            Icon(
                                imageVector = Icons.Filled.Group,
                                contentDescription = "",
                                tint = Color.LightGray,
                                modifier = Modifier.size(50.dp)
                            )

                            Text(
                                text = "No friends yet, add some!",
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
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
                                            .clickable {
                                                openFriendProfile.value = true
                                                currentFriendID.value = addedFriend
                                                currentFriendPage.value = friend.value
                                                displayedFriendFile.value = displayedFriend.value
                                            },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Add Friend",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column() {
                Row() {
                    CustomOutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Enter a username",
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
                                        Toast.makeText(
                                            contextForToast,
                                            "User not found with that username.",
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
fun removeFriendDialog(){
    val contextForToast = LocalContext.current.applicationContext
    val db = Firebase.firestore

    AlertDialog(
        onDismissRequest = {
            openRemoveFriendDialog.value = false
        },
        title = {
            Text(
                text = "Remove Friend",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "Are you sure you want to remove this friend?",
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


                friendDocumentReference.get().addOnSuccessListener { documentSnapshot ->
                    currentFriendPage.value = documentSnapshot.toObject<TravelyzeUser>()!!


                    if (currentUser.value.data?.friendsList?.contains(
                            currentFriendID.value) == true &&
                        currentFriendPage.value.data?.friendsList?.contains(
                            userID) == true
                    ) {
                        //Remove request from users respective lists
                        currentUser.value.data?.friendsList?.remove(currentFriendID.value)

                        currentFriendPage.value.data?.friendsList?.remove(userID)

                        //Update accounts
                        currUserDocumentReference.set(currentUser.value)
                        friendDocumentReference.set(currentFriendPage.value)

                        openRemoveFriendDialog.value = false
                        openFriendProfile.value = false
                        Toast.makeText(
                            contextForToast,
                            "You are no longer friends",
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
                openRemoveFriendDialog.value = false
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
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun displayFriendProfile(){
    Dialog(
        onDismissRequest = {
            openFriendProfile.value = false
        }
    ){
        Scaffold(
            modifier = Modifier
                .padding(20.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .fillMaxSize(),
            topBar = {
                     TopBar(
                         buttonIcon = Icons.Filled.PersonRemove,
                         onButtonClicked = {
                             openRemoveFriendDialog.value = true
                         }
                     )
            },
            content = {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(color = BackgroundColor)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 30.dp, vertical = 50.dp)
                            .background(color = BackgroundColor),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model =
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(displayedFriendFile.value)
                                        .size(Size.ORIGINAL)
                                        .build()
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Column(
                            modifier = Modifier.padding(start = 20.dp)
                        ) {
                            Text(
                                text = "${currentFriendPage.value.info?.firstName} ${currentFriendPage.value.info?.lastName}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontFamily = halcomFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                            Text(
                                text = "@${currentFriendPage.value.info?.userName}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontFamily = halcomFamily,
                                fontWeight = FontWeight.Light,
                                color = Color.Black
                            )
                        }
                    }

                    Row( modifier = Modifier
                        .padding(start = 30.dp, bottom = 10.dp, end = 30.dp)
                        .background(color = BackgroundColor)){
                        Text(
                            text = "Favorite Locations",
                            style = MaterialTheme.typography.headlineMedium,
                            fontFamily = halcomFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .horizontalScroll(rememberScrollState())
                            .fillMaxHeight()
                    ){
                        if(!currentFriendPage.value.data?.favoriteLocations.isNullOrEmpty()){
                            currentFriendPage.value.data?.favoriteLocations?.forEach { name ->
                                var location = locationList.find { it.Name.equals(name) }
                                androidx.compose.material.Card(
                                    modifier = Modifier
                                        .size(width = 250.dp, height = 150.dp)
                                        .padding(10.dp)
                                        .clickable {
                                            profileSelectedName.value = name
                                            profileLocationSelected.value = true
                                        },
                                    shape = RoundedCornerShape(15.dp),
                                    elevation = 5.dp
                                ) {
                                    Column() {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            AsyncImage(
                                                model =
                                                ImageRequest.Builder(LocalContext.current)
                                                    .decoderFactory(SvgDecoder.Factory())
                                                    .data(location?.Flag)
                                                    .build(),
                                                filterQuality = FilterQuality.None,
                                                contentScale = ContentScale.FillBounds,
                                                contentDescription = null,
                                            )
                                        }
                                    }
                                }
                            }
                        }else{
                            Row( modifier = Modifier
                                .padding(start = 10.dp, bottom = 10.dp)
                                .background(color = BackgroundColor)){
                                Text(
                                    text = "This user doesn't have any favorite locations.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = halcomFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                }
            }
        )
    }
}

