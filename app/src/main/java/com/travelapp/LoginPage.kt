package com.travelapp

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.travelapp.composable.CustomOutlinedTextField
import com.travelapp.composable.TravelyzeUser
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.TextButtonColor
import com.travelapp.ui.theme.robotoFamily

val isRegistering = mutableStateOf(false)
val openPasswordResetDialog = mutableStateOf(false)

@Composable
fun Login(auth: FirebaseAuth, nav: NavController) {

    var focusManager = LocalFocusManager.current


    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val isEmailValid by remember {
        derivedStateOf {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    var isPasswordValid by remember {
        mutableStateOf(true)
    }

    var loginErrorMessage by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }


    if (openPasswordResetDialog.value) {
        resetPasswordDialog(auth)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp, 150.dp)
            )
        }

        //
        // Welcome Message
        //
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier
                    .padding(20.dp)
                    .width(500.dp),
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Light,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                text = "Log In"
            )
        }

        //
        // Email TextField
        //
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            placeholder = { Text("example@domain.com") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = TextButtonColor,
                cursorColor = TextButtonColor,
                focusedLabelColor = TextButtonColor),
            modifier = Modifier
                .fillMaxWidth(.6f)
        )

        //
        // Password TextField
        //
        CustomOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            modifier = Modifier
                .fillMaxWidth(.6f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() },
                onDone = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val fireStore = FirebaseFirestore.getInstance()

                                val userID = Firebase.auth.currentUser?.uid.toString()
                                val documentReference = fireStore.collection("users").document(userID)

                                documentReference.get().addOnSuccessListener { documentSnapshot ->
                                    currentUser.value = documentSnapshot.toObject<TravelyzeUser>()!!
                                }

                                isLoggedIn.value = true
                                isPasswordValid = true
                                loginErrorMessage = ""
                            } else {
                                isPasswordValid = false
                                loginErrorMessage = "Invalid login credentials."
                            }
                        }
                }
            ),
            showError = !isPasswordValid,
            errorMessage = loginErrorMessage,
            isPasswordField = true,
            isPasswordVisible = isPasswordVisible,
            onVisibilityChange = { isPasswordVisible = it },

            leadingIconImageVector = Icons.Filled.Password,
            leadingIconDescription = if (isPasswordVisible) "Hide password" else "Show password"
        )

        Row(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            //
            // Login Button
            //
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val fireStore = FirebaseFirestore.getInstance()

                                val userID = Firebase.auth.currentUser?.uid.toString()
                                val documentReference = fireStore.collection("users").document(userID)

                                documentReference.get().addOnSuccessListener { documentSnapshot ->
                                    currentUser.value = documentSnapshot.toObject<TravelyzeUser>()!!
                                }

                                isLoggedIn.value = true
                                isPasswordValid = true
                                loginErrorMessage = ""
                            } else {
                                isPasswordValid = false
                                loginErrorMessage = "Invalid login credentials."
                            }
                        }
                },
                modifier = Modifier
                    .size(width = 150.dp, height = 50.dp)
                    .padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = TextButtonColor),
                enabled = isEmailValid && password.isNotEmpty()
            ) {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    nav.navigate(Screen.Register.route) {
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                enabled = true,
                modifier = Modifier
                    .size(width = 150.dp, height = 50.dp)
                    .padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = TextButtonColor)
            ) {
                Text(
                    text = "Register",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        TextButton(
            onClick = {
                openPasswordResetDialog.value = true
            }
        ) {
            Text(
                text = "Forgot password?",
                color = Color.Black,
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun resetPasswordDialog(auth: FirebaseAuth) {
    val contextForToast = LocalContext.current.applicationContext

    var email by remember {
        mutableStateOf("")
    }

    var showEmailError by rememberSaveable { mutableStateOf(false) }

    var emailError = "We could not find an account with that email address"

    AlertDialog(
        onDismissRequest = {
            openPasswordResetDialog.value = false
        },
        title = {
            Text(
                text = "Password reset",
                fontFamily = robotoFamily,
                color = Color.Black
            )
        },
        text = {
            Column() {
                Row() {
                    Text(
                        text = "Please enter the email address associated with your account.",
                        fontFamily = robotoFamily,
                        color = Color.Black
                    )
                }
                Row() {
                    CustomOutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        showError = showEmailError,
                        errorMessage = emailError,
                        leadingIconImageVector = Icons.Default.AlternateEmail,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { /*TODO focusmanager not working? */ }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (email.isNotBlank()) {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener {
                        if (it.isSuccessful) {
                            openPasswordResetDialog.value = false
                            showEmailError = false

                            Toast.makeText(
                                contextForToast,
                                "Password recovery email sent.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            emailError = "Please enter a valid email."
                            showEmailError = true
                        }
                    }
                } else {
                    emailError = "Please enter a valid email."
                    showEmailError = true
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
                openPasswordResetDialog.value = false
            }) {
                Text(
                    text = "CANCEL",
                    fontFamily = robotoFamily,
                    color = TextButtonColor,
                )
            }
        }
    )
}
