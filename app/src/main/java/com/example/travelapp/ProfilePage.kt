package com.example.travelapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.travelapp.composable.CustomOutlinedTextField
import com.example.travelapp.composable.TopBar
import com.example.travelapp.composable.TravelyzeUser
import com.example.travelapp.ui.theme.BackgroundColor
import com.example.travelapp.ui.theme.TextButtonColor
import com.example.travelapp.ui.theme.marsFamily
import com.example.travelapp.ui.theme.robotoFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

var openSignoutDialog = mutableStateOf(false)
var openDeleteDialog = mutableStateOf(false)
val openEditDialog = mutableStateOf(false)
val openPicDialog = mutableStateOf(false)
val openPicSelectDialog = mutableStateOf(false)
val openPicTakenDialog = mutableStateOf(false)
val isDrawerOpen = mutableStateOf(false)
val sendPasswordChangeEmail = mutableStateOf(false)

var profileImageUri = mutableStateOf(Uri.EMPTY)
var takenImageUri = mutableStateOf(Uri.EMPTY)
var displayedPicture = mutableStateOf<File>(File(""))

@Composable
fun Profile(auth: FirebaseAuth){

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()



    fun openDrawer()  {
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
        SendEmailToExistingUser(Firebase.auth.currentUser)
    }

    if (profileImageUri.value.path?.isNotEmpty() == true) {
        openPicDialog.value = false
        openPicSelectDialog.value = true
    }

    if (takenImageUri.value.path?.isNotEmpty() == true) {
        openPicDialog.value = false
        openPicTakenDialog.value = true
    }

    val fireStore = FirebaseFirestore.getInstance()

    val userID = Firebase.auth.currentUser?.uid.toString()
    val documentReference = fireStore.collection("users").document(userID)

    documentReference.get().addOnSuccessListener { documentSnapshot ->
        //TODO Figure out how to use this variable outside this listener
        documentSnapshot.toObject<TravelyzeUser>()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        var profileImage = Firebase.storage.reference.child("users/${auth.currentUser?.uid}/profile_picture.jpg")

        profileImage.getFile(profileImageFile.value).addOnCompleteListener {
            displayedPicture.value = profileImageFile.value
        }

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
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr){
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
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
                    Column(
                        Modifier.background(color = BackgroundColor)
                    ){
                        Row(
                            modifier = Modifier.padding(horizontal = 30.dp, vertical = 50.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Box {
                                Image(
                                    painter = rememberAsyncImagePainter(model =
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(displayedPicture.value)
                                        .size(Size.ORIGINAL)
                                        .build()
                                    ),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(75.dp)
                                        .clip(CircleShape)
                                )
                                Icon(Icons.Filled.Edit,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clickable {
                                            openPicDialog.value = true
                                        }
                                        .align(Alignment.BottomEnd)
                                        .size(25.dp)
                                        .background(color = Color.White, shape = CircleShape))
                            }
                            Column(
                                modifier = Modifier.padding(start = 20.dp)
                            ){
                                Text(
                                    text = auth.currentUser?.displayName!!,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontFamily = robotoFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                                Text(
                                    text = auth.currentUser?.email!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = robotoFamily,
                                    fontWeight = FontWeight.Light,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun ProfilePicturePicker(auth: FirebaseAuth){
    LocalContext.current.applicationContext

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) profileImageUri.value = uri }
    )

    val context = LocalContext.current
    val file = File.createTempFile(
        "JPEG_" + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()),
        ".jpg",
        context.externalCacheDir
    )
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) takenImageUri.value = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        }
    }

    AlertDialog(
        onDismissRequest = {
            openPicDialog.value = false
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Change photo",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(thickness = 2.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    TextButton(onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }){
                        Icon(imageVector = Icons.Filled.Folder,
                            contentDescription = null,
                            modifier = Modifier.size(size = 30.dp).padding(end = 10.dp),
                            tint = Color.Black)
                        Text(
                            text = "Upload from camera roll",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
                Divider(thickness = 2.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    TextButton(onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }){
                        Icon(imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier.size(size = 30.dp).padding(end = 10.dp),
                            tint = Color.Black)
                        Text(
                            text = "Take a new picture",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
                Divider(thickness = 2.dp)
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}

@Composable
fun ProfilePicSelect(auth: FirebaseAuth){
    val contextForToast = LocalContext.current.applicationContext
    var storage = Firebase.storage

    AlertDialog(
        onDismissRequest = {
            profileImageUri.value = Uri.EMPTY
            openPicSelectDialog.value = false
        },
        title = {
            Text(
                text = "Use this picture?",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    painter = rememberAsyncImagePainter(profileImageUri.value),
                    contentDescription = null
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                profileImageUri.value = Uri.EMPTY
                openPicSelectDialog.value = false
            }) {
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val profilePicRef = storage.reference.child("users/${auth.currentUser?.uid}/profile_picture.jpg")
                val uploadTask = profilePicRef.putFile(profileImageUri.value)
                uploadTask.addOnSuccessListener {
                    Toast.makeText(
                        contextForToast,
                        "Your picture has been updated.",
                        Toast.LENGTH_SHORT
                    ).show()

                    profileImageUri.value = Uri.EMPTY
                    openPicSelectDialog.value = false
                }.addOnFailureListener() {
                    Toast.makeText(
                        contextForToast,
                        "Something went wrong changing your picture.",
                        Toast.LENGTH_SHORT
                    ).show()
                    profileImageUri.value = Uri.EMPTY
                    openPicSelectDialog.value = false
                }
            }) {
                Text(
                    text = "CONFIRM",
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
fun ProfilePicTaken(auth: FirebaseAuth){
    val contextForToast = LocalContext.current.applicationContext
    var storage = Firebase.storage

    AlertDialog(
        onDismissRequest = {
            takenImageUri.value = Uri.EMPTY
            openPicTakenDialog.value = false
        },
        title = {
            Text(
                text = "Use this picture?",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Box {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        painter = rememberAsyncImagePainter(takenImageUri.value),
                        contentDescription = null
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = {
                takenImageUri.value = Uri.EMPTY
                openPicTakenDialog.value = false
            }) {
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = Color.Red,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val profilePicRef = storage.reference.child("users/${auth.currentUser?.uid}/profile_picture.jpg")
                val uploadTask = profilePicRef.putFile(takenImageUri.value)
                uploadTask.addOnSuccessListener {
                    Toast.makeText(
                        contextForToast,
                        "Your picture has been updated.",
                        Toast.LENGTH_SHORT
                    ).show()

                    takenImageUri.value = Uri.EMPTY
                    openPicTakenDialog.value = false
                }.addOnFailureListener() {
                    Toast.makeText(
                        contextForToast,
                        "Something went wrong changing your picture.",
                        Toast.LENGTH_SHORT
                    ).show()
                    takenImageUri.value = Uri.EMPTY
                    openPicTakenDialog.value = false
                }
            }) {
                Text(
                    text = "CONFIRM",
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
fun SignOutDialog(fireBaseAuth: FirebaseAuth){
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
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}

@Composable
fun DeleteAccountDialog(fireBaseAuth: FirebaseAuth){
    val contextForToast = LocalContext.current.applicationContext
    val focusManager = LocalFocusManager.current

    var username by remember {
        mutableStateOf("")
    }

    var validateUsernameError by rememberSaveable { mutableStateOf(true) }

    val usernameError = "This username does not match your current username."

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
            Column {
                Row {
                    Text(
                        text = "Are you sure you wish to PERMANENTLY delete your account? This cannot be undone.",
                        fontFamily = robotoFamily,
                        color = Color.Black
                    )
                }
                Row {
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
                    val fireStore = FirebaseFirestore.getInstance()

                val userID = Firebase.auth.currentUser?.uid.toString()

                val documentReference = fireStore.collection("users").document(userID)

                documentReference.get().addOnSuccessListener { documentSnapshot ->

                    val user = documentSnapshot.toObject<TravelyzeUser>()

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
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}

@Composable
fun EditUsernameDialog() {
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
            Column {
                Row {
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

                val fireStore = FirebaseFirestore.getInstance()

                val userID = Firebase.auth.currentUser?.uid.toString()

                val documentReference = fireStore.collection("users").document(userID)


                documentReference.get().addOnSuccessListener { documentSnapshot ->

                    val user = documentSnapshot.toObject<TravelyzeUser>()

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
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}

@Composable
fun SendEmailToExistingUser(user: FirebaseUser?){
    val contextForToast = LocalContext.current.applicationContext
    var isEmailPassword = false
    val email = user?.email.toString()

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





