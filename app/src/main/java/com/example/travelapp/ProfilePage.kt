package com.example.travelapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelapp.composable.Drawer
import com.example.travelapp.composable.TopBar
import com.example.travelapp.composable.TravelyzeUser
import com.example.travelapp.ui.theme.Aero
import com.example.travelapp.ui.theme.Alabaster
import com.example.travelapp.ui.theme.SoftWhite
import com.example.travelapp.ui.theme.robotoFamily
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
val isDrawerOpen = mutableStateOf(false)
val sendPasswordChangeEmail = mutableStateOf(false)

@Composable
fun Profile(){

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
        sendEmailToExistingUser(Firebase.auth.currentUser)
    }

    var fireStore = FirebaseFirestore.getInstance()

    var userID = Firebase.auth.currentUser?.uid.toString()
    Log.d("userID", "$userID")
    var documentReference = fireStore.collection("users").document(userID)

    documentReference.get().addOnSuccessListener { documentSnapshot ->

        Log.d("documentSnapshot", "$documentSnapshot")
        var user = documentSnapshot.toObject<TravelyzeUser>()
        Log.d("user", "$user")

    }


    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(color = SoftWhite)
    ) {
        TopBar(
            title = "Profile",
            buttonIcon = Icons.Outlined.Menu,
            onButtonClicked = {
                isDrawerOpen.value = !isDrawerOpen.value
            }
        )

        Box(Modifier.fillMaxSize()){
            //
            // Profile Box
            //
            Row(Modifier.fillMaxWidth()){
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, top = 10.dp, end = 30.dp, bottom = 30.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Alabaster)
                        .border(2.dp, SolidColor(Color.Black), shape = RoundedCornerShape(20.dp))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)

                    ) {
                        //TODO Create a method of editing the profile page
                        Image(
                            painter = rememberAsyncImagePainter("https://www.theshirtlist.com/wp-content/uploads/2018/12/Rowlet.jpg"),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(5.dp, Color.White, CircleShape)
                        )
                        Text(text = "Gabriel Madeira",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        )


                    }

                    Row(Modifier.fillMaxWidth()) {
                        Divider(startIndent = 0.dp, thickness = 2.dp, color = Color.Black)
                    }

                    Column( Modifier
                        .fillMaxWidth(),
                    ) {
                        Text(text = "Favorites",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                        )

                        Text(text = "\tPortland, OR, USA\n\n\tHartford, CT, USA\n\n\tStockholm, SE\n\n\tEncino, CA, USA\n\n\tSão Paulo, Brazil",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(10.dp))
                    }
                }
            }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl, ) {
                ModalDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = drawerState.isOpen,
                    modifier = Modifier.fillMaxHeight(),
                    drawerContent = {
                        Drawer(
                            modifier = Modifier.background(color = Alabaster),
                            Firebase.auth
                        )
                    }
                ){
                    //Do nothing
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
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "You will have to log back in to use some features.",
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
                    color = Aero,
                )
            }
        }
    )
}

@Composable
fun deleteAccountDialog(fireBaseAuth: FirebaseAuth){
    val contextForToast = LocalContext.current.applicationContext
    val focusManager = LocalFocusManager.current

    var password by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by rememberSaveable { mutableStateOf(false)}

    Dialog(
        onDismissRequest = {
            openDeleteDialog.value = false
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
                        .height(250.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White)
                        .border(2.dp, SolidColor(Color.Black), shape = RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center,
                ){
                    Text(
                        modifier = Modifier.padding(bottom = 120.dp, start = 10.dp, end = 10.dp),
                        text = "Are you sure you wish to DELETE your account permanently?",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Enter Password to confirm") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(.65f)
                            .padding(top = 15.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType =  KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.clearFocus() }
                        ),
                        //isError = !isPasswordValid,
                        //visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),

                        trailingIcon = {
                            val image = if (isPasswordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description = if (isPasswordVisible) "Hide password" else "Show password"

                            IconButton(onClick = {isPasswordVisible = !isPasswordVisible}){
                                Icon(imageVector  = image, description)
                            }
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, top = 165.dp, end = 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Button(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(end = 10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Aero),
                            onClick = {
                                if(
                                    //TODO ensure entered password matches saved password
                                    password == "Gm@123456"
                                ){
                                    fireBaseAuth.currentUser?.delete()
                                } else{
                                    //TODO Inform that password was entered incorrectly
                                }

                                openDeleteDialog.value = false
                                Toast.makeText(
                                    contextForToast,
                                    "Account successfully deleted.",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                                openDeleteDialog.value = false
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

//fun getUserAccount(): TravelyzeUser? {
//    var fireStore = FirebaseFirestore.getInstance()
//
//    var userID = Firebase.auth.currentUser?.uid.toString()
//    var documentReference = fireStore.collection("users").document(userID)
//
//    var user: TravelyzeUser?
//
//    documentReference.get().addOnSuccessListener { documentSnapshot ->
//
//        user = documentSnapshot.toObject<TravelyzeUser>()
//
//    }
//
//    return user
//}

