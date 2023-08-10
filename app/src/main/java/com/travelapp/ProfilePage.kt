package com.travelapp

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
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
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import com.travelapp.composable.CustomOutlinedTextField
import com.travelapp.composable.TopBar
import com.travelapp.ui.theme.*
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

var profileLocationSelected = mutableStateOf(false)
var profileSelectedName = mutableStateOf("")

/**
 * Displays the logged in user's profile.
 *
 * @param auth The [FirebaseAuth] instance used to get information about the current user state
 */
@Composable
fun Profile(auth: FirebaseAuth) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    fun openDrawer() {
        scope.launch {
            drawerState.open()
        }
    }

    fun closeDrawer() {
        scope.launch {
            drawerState.close()
        }
    }

    if (isDrawerOpen.value) {
        openDrawer()
    } else {
        closeDrawer()
    }

    if (sendPasswordChangeEmail.value) {
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

    /** Used to display the settings drawer on the right side of the screen */
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            modifier = Modifier.fillMaxHeight(),
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    drawerShape = RectangleShape
                ) {
                    Spacer(Modifier.height(30.dp))
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Edit Username",
                                fontFamily = robotoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        onClick = {
                            openEditDialog.value = true
                        },
                        icon = { Icon(Icons.Filled.Badge, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Change Password",
                                fontFamily = robotoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        onClick = {
                            sendPasswordChangeEmail.value = true
                        },
                        icon = { Icon(Icons.Filled.Password, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Delete Account",
                                fontFamily = robotoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        onClick = {
                            openDeleteDialog.value = true
                        },
                        icon = { Icon(Icons.Filled.Delete, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 30.dp, horizontal = 20.dp),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Sign Out",
                                fontFamily = robotoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        },
                        onClick = {
                            openSignoutDialog.value = true
                        },
                        icon = { Icon(Icons.Filled.DoorFront, null) },
                        selected = false,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        ) {
            /** Switch back to regular display orientation (left to right) */
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Column(
                    Modifier
                        .fillMaxSize()
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
                    Column() {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 50.dp)
                                .background(color = BackgroundColor),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box {
                                CoilImage(
                                    imageModel = {displayedPicture.value},
                                    component = rememberImageComponent {
                                        +ShimmerPlugin(
                                            baseColor = BackgroundColor,
                                            highlightColor = Color.LightGray,
                                            durationMillis = 350,
                                            dropOff = 0.65f,
                                            tilt = 20f
                                        )
                                    },
                                    imageOptions = ImageOptions(
                                        contentScale = ContentScale.Crop
                                    ),
                                    modifier = Modifier
                                        .size(75.dp)
                                        .clip(CircleShape)
                                )
                                Icon(Icons.Filled.AddAPhoto,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clickable {
                                            openPicDialog.value = true
                                        }
                                        .align(Alignment.BottomEnd)
                                        .size(20.dp)
                                        .background(color = Color.Black, shape = CircleShape)
                                        .padding(3.dp),
                                    tint = Color.White)
                            }
                            Column(
                                modifier = Modifier.padding(start = 20.dp)
                            ) {
                                Text(
                                    text = "${currentUser.value.info?.firstName} ${currentUser.value.info?.lastName}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontFamily = halcomFamily,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                                Text(
                                    text = "@${currentUser.value.info?.userName}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = halcomFamily,
                                    fontWeight = FontWeight.Light,
                                    color = Color.Black
                                )
                            }
                        }

                        Row( modifier = Modifier
                            .padding(start = 30.dp, bottom = 10.dp)
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
                            if(!currentUser.value.data?.favoriteLocations.isNullOrEmpty()){
                                currentUser.value.data?.favoriteLocations?.forEach { name ->
                                    var location = locationList.find { it.Name.equals(name) }
                                    Card(
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
                                            Row(verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Start,
                                                modifier = Modifier
                                                    .fillMaxWidth()){
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
                                        text = "You don't have any. Go find some!",
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
            }
        }
    }
}

/**
 * An [AlertDialog] used to prompt the user for a new profile picture. Can select picture
 * from camera roll using [PickVisualMediaRequest] or take a picture from camera using
 * [ActivityResultContracts.TakePicture]
 */
@SuppressLint("SimpleDateFormat")
@Composable
fun ProfilePicturePicker() {
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
            ) {
                Text(
                    text = "Change photo",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Normal,
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
                ) {
                    TextButton(onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)) {
                        Icon(
                            imageVector = Icons.Filled.Folder,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size = 30.dp)
                                .padding(end = 10.dp),
                        )
                        Text(
                            text = "Upload from camera roll",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Divider(thickness = 2.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)) {
                        Icon(
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size = 30.dp)
                                .padding(end = 10.dp),
                        )
                        Text(
                            text = "Take a new picture",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Bold,
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

/**
 * An [AlertDialog] used to confirm camera roll photo selection.
 *
 * @param auth The [FirebaseAuth] instance used to update the user's profile image
 */
@Composable
fun ProfilePicSelect(auth: FirebaseAuth) {
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
            )
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
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
                val profilePicRef =
                    storage.reference.child("users/${auth.currentUser?.uid}/profile_picture.jpg")
                val uploadTask = profilePicRef.putFile(profileImageUri.value)
                uploadTask.addOnSuccessListener {
                    Toast.makeText(
                        contextForToast,
                        "Your picture has been updated.",
                        Toast.LENGTH_SHORT
                    ).show()

                    profileImageUri.value = Uri.EMPTY

                    var profileImage =
                        Firebase.storage.reference.child("users/${auth.currentUser?.uid}/profile_picture.jpg")

                    profileImage.getFile(profileImageFile.value).addOnCompleteListener {
                        displayedPicture.value = profileImageFile.value
                    }

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

/**
 * An [AlertDialog] used to confirm photo selection from camera.
 *
 * @param auth The [FirebaseAuth] instance used to update the user's profile image
 */
@Composable
fun ProfilePicTaken(auth: FirebaseAuth) {
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
            )
        },
        text = {
            Box {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
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
                val profilePicRef =
                    storage.reference.child("users/${auth.currentUser?.uid}/profile_picture.jpg")
                val uploadTask = profilePicRef.putFile(takenImageUri.value)
                uploadTask.addOnSuccessListener {
                    Toast.makeText(
                        contextForToast,
                        "Your picture has been updated.",
                        Toast.LENGTH_SHORT
                    ).show()

                    takenImageUri.value = Uri.EMPTY

                    var profileImage =
                        Firebase.storage.reference.child("users/${auth.currentUser?.uid}/profile_picture.jpg")

                    profileImage.getFile(profileImageFile.value).addOnCompleteListener {
                        displayedPicture.value = profileImageFile.value
                    }

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

/**
 * An [AlertDialog] used to prompt the user to sign out.
 *
 * @param auth The [FirebaseAuth] instance used to sign the user out
 * of the application
 */
@Composable
fun SignOutDialog(auth: FirebaseAuth) {
    val contextForToast = LocalContext.current.applicationContext

    AlertDialog(
        onDismissRequest = {
            openDeleteDialog.value = false
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Row {
                Text(
                    text = "Are you sure you want to sign out?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = robotoFamily,
                    textAlign = TextAlign.Center,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                auth.signOut()
                isLoggedIn.value = Firebase.auth.currentUser != null
                openSignoutDialog.value = false
                Toast.makeText(
                    contextForToast,
                    "You are now logged out",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
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
 * An [AlertDialog] used to prompt the user to delete their account. This is
 * an irreversible action that WILL permanently delete all of the user's data.
 *
 * @param auth The [FirebaseAuth] instance used to delete the user's account
 */
@Composable
fun DeleteAccountDialog(auth: FirebaseAuth) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Delete Account",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column {
                Row {
                    Text(
                        text = "Are you sure you wish to PERMANENTLY delete your account? This cannot be undone.",
                        fontFamily = robotoFamily,
                        textAlign = TextAlign.Center,
                    )
                }
                Row {
                    CustomOutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = "Enter your username",
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
                        modifier = Modifier.fillMaxWidth(),
                        isDialog = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val fireStore = FirebaseFirestore.getInstance()

                val userID = Firebase.auth.currentUser?.uid.toString()

                val documentReference = fireStore.collection("users").document(userID)

                if (username == currentUser.value.info?.userName) {
                    auth.currentUser?.delete()
                    documentReference.delete()

                    openDeleteDialog.value = false
                    isLoggedIn.value = false
                    Toast.makeText(
                        contextForToast,
                        "Account successfully deleted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    validateUsernameError = false
                }
            }) {
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
 * An [AlertDialog] that prompts the user to change their username.
 */
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Edit Username",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
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
                        label = "Enter new username",
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        isDialog = true
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if(username.isNotBlank()){
                    val db = Firebase.firestore

                    val docRef = db.collection("users")
                        .whereEqualTo("info.userName", username)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.size() == 0) {
                                openEditDialog.value = false

                                val fireStore = FirebaseFirestore.getInstance()

                                val userID = Firebase.auth.currentUser?.uid.toString()

                                val documentReference = fireStore.collection("users").document(userID)

                                currentUser.value.info?.userName = username
                                documentReference.set(currentUser.value)

                                Toast.makeText(
                                    contextForToast,
                                    "Username successfully changed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    contextForToast,
                                    "Username already taken.",
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
            }) {
                Text(
                    text = "CONFIRM",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openEditDialog.value = false
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
 * Sends the user an email to reset their password.
 * Displays a [Toast] with a resulting message.
 *
 * @param user The [FirebaseUser] object that contains the recipient email
 */
@Composable
fun SendEmailToExistingUser(user: FirebaseUser?) {
    val contextForToast = LocalContext.current.applicationContext
    var isEmailPassword = false
    val email = user?.email.toString()

    if (user != null) {
        for (profile: UserInfo in user.providerData) {
            if (profile.providerId == "password") {
                isEmailPassword = true
            }
        }
    }

    if (isEmailPassword) {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
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



