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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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

var openSignoutDialog = mutableStateOf(false)
var openDeleteDialog = mutableStateOf(false)
val openEditDialog = mutableStateOf(false)
val isDrawerOpen = mutableStateOf(false)
val sendPasswordChangeEmail = mutableStateOf(false)


@Composable
fun Profile(){

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun openDrawer()  {
        Log.w("open", "opened")
        scope.launch {
            drawerState.open()
        }
    }
    fun closeDrawer(){
        scope.launch {
            drawerState.close()
        }
    }

    if(isDrawerOpen.value){
        openDrawer()
    } else {
        closeDrawer()
    }

    if(sendPasswordChangeEmail.value){
        sendEmailToExistingUser(Firebase.auth.currentUser)
    }

    var fireStore = FirebaseFirestore.getInstance()

    var userID = Firebase.auth.currentUser?.uid.toString()
    Log.d("userID", "$userID")
    var documentReference = fireStore.collection("users").document(userID)

    documentReference.get().addOnSuccessListener { documentSnapshot ->
        //TODO Figure out how to use this variable outside this listener
        var user = documentSnapshot.toObject<TravelyzeUser>()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            modifier = Modifier.fillMaxHeight(),
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    drawerShape = RectangleShape
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 30.dp, bottom = 30.dp)
                            .width(200.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontFamily = marsFamily,
                        text = "Settings"
                    )
                    Spacer(Modifier.height(30.dp))
                    NavigationDrawerItem(
                        label = {Text(
                            text = "Edit Username",
                            fontFamily = robotoFamily,
                            fontSize = 20.sp)},
                        onClick = {
                            openEditDialog.value = true
                        },
                        icon = { Icon(Icons.Filled.Badge, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = {Text(
                            text = "Change Password",
                            fontFamily = robotoFamily,
                            fontSize = 20.sp)},
                        onClick = {
                            sendPasswordChangeEmail.value = true
                        },
                        icon = { Icon(Icons.Filled.Password, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = {Text(
                            text = "Sign Out",
                            fontFamily = robotoFamily,
                            fontSize = 20.sp)},
                        onClick = {
                            openSignoutDialog.value = true
                        },
                        icon = { Icon(Icons.Filled.DoorFront, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 50.dp, horizontal = 20.dp),
                        color = Color.Gray,
                        thickness = 1.dp)
                    NavigationDrawerItem(
                        label = {Text(
                            text = "Delete Account",
                            fontFamily = robotoFamily,
                            fontSize = 20.sp)},
                        onClick = {
                            openDeleteDialog.value = true
                        },
                        icon = { Icon(Icons.Filled.Delete, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        ){
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr, ){
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .background(color = BackgroundColor)
                ) {
                    TopBar(
                        title = "Profile",
                        buttonIcon = Icons.Filled.Settings,
                        onButtonClicked = {
                            openDrawer()
                        }
                    )
                    Column(){
                        Row(){
                            Image(
                                painter = rememberAsyncImagePainter(model = Firebase.auth.currentUser?.photoUrl),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(75.dp)
                                    .clip(CircleShape)
                                    .border(5.dp, Color.White, CircleShape)
                            )
                            Box(){
                                Icon(Icons.Filled.Camera, contentDescription = "Check mark")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun signOutDialog(fireBaseAuth: FirebaseAuth){
    val contextForToast = LocalContext.current.applicationContext

    AlertDialog(
        onDismissRequest = {
            openDeleteDialog.value = false
        },
        title = {
            Text(
                text = "Are you sure you want to sign out?",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "You will have to log back in to use some features.",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        confirmButton = {
            TextButton(onClick = {
                fireBaseAuth.signOut()
                isLoggedIn.value = Firebase.auth.currentUser != null
                openSignoutDialog.value = false
                Toast.makeText(
                    contextForToast,
                    "You are now logged out",
                    Toast.LENGTH_SHORT
                ).show()
            }){
                Text(
                    text = "SIGN OUT",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openSignoutDialog.value = false
            }){
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        }
    )
}

@Composable
fun deleteAccountDialog(fireBaseAuth: FirebaseAuth){
    val contextForToast = LocalContext.current.applicationContext
    val focusManager = LocalFocusManager.current

    var username by remember {
        mutableStateOf("")
    }

    var validateUsernameError by rememberSaveable { mutableStateOf(true) }

    var usernameError = "This username does not match your current username."

    AlertDialog(
        onDismissRequest = {
            openDeleteDialog.value = false
        },
        title = {
            Text(
                text = "Delete Account",
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Column(){
                Row(){
                    Text(
                        text = "Are you sure you wish to PERMANENTLY delete your account? This cannot be undone.",
                        fontFamily = robotoFamily,
                        color = Color.Black
                    )
                }
                Row(){
                    CustomOutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Please enter your username",
                        showError = !validateUsernameError,
                        errorMessage = usernameError,
                        leadingIconImageVector = Icons.Default.ManageAccounts,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                var fireStore = FirebaseFirestore.getInstance()

                var userID = Firebase.auth.currentUser?.uid.toString()

                var documentReference = fireStore.collection("users").document(userID)

                documentReference.get().addOnSuccessListener { documentSnapshot ->

                    var user = documentSnapshot.toObject<TravelyzeUser>()

                    if( username == user?.info?.userName )
                    {
                        fireBaseAuth.currentUser?.delete()
                        documentReference.delete()

                        openDeleteDialog.value = false
                        isLoggedIn.value = false
                        Toast.makeText(
                            contextForToast,
                            "Account successfully deleted.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else{
                        validateUsernameError = false
                    }


                }
            }){
                Text(
                    text = "CONFIRM",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openDeleteDialog.value = false
            }){
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        }
    )
}

@Composable
fun editUsernameDialog() {
    val contextForToast = LocalContext.current.applicationContext
    val focusManager = LocalFocusManager.current

    var username by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            openEditDialog.value = false
        },
        title = {
            Text(
                text = "Enter a new username:",
                fontFamily = robotoFamily,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        },
        text = {
            Column(){
                Row(){
                    CustomOutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        leadingIconImageVector = Icons.Default.ManageAccounts,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                openEditDialog.value = false

                var fireStore = FirebaseFirestore.getInstance()

                var userID = Firebase.auth.currentUser?.uid.toString()

                var documentReference = fireStore.collection("users").document(userID)


                documentReference.get().addOnSuccessListener { documentSnapshot ->

                    var user = documentSnapshot.toObject<TravelyzeUser>()

                    user?.info?.userName = username

                    if (user != null) {
                        documentReference.set(user)
                    }
                }


                Toast.makeText(
                    contextForToast,
                    "Username successfully changed.",
                    Toast.LENGTH_SHORT
                ).show()
            }){
                Text(
                    text = "CONFIRM",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openEditDialog.value = false
            }){
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        }
    )
}

@Composable
fun sendEmailToExistingUser(user: FirebaseUser?){
    val contextForToast = LocalContext.current.applicationContext
    var isEmailPassword = false
    var email = user?.email.toString()

    if(user != null) {
        for (profile:UserInfo in user.providerData) {
            if(profile.providerId == "password"){
                isEmailPassword = true
            }
        }
    }

    if(isEmailPassword){
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener{
            if(it.isSuccessful) {
                Toast.makeText(
                    contextForToast,
                    "Password change email sent.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            sendPasswordChangeEmail.value = false
        }
    }
}





