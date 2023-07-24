package com.example.travelapp

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp.MainActivity.Companion.TAG
import com.example.travelapp.composable.CustomOutlinedTextField
import com.example.travelapp.ui.theme.Aero
import com.example.travelapp.ui.theme.SoftWhite
import com.example.travelapp.ui.theme.robotoFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

val isRegistering = mutableStateOf(false)
val openPasswordResetDialog = mutableStateOf(false)

@Composable
fun Login(auth: FirebaseAuth){
    var focusManager= LocalFocusManager.current


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

    val isPasswordValid by remember {
        derivedStateOf {
            password.length > 7
        }
    }

    var isPasswordVisible by rememberSaveable { mutableStateOf(false)}


    if(openPasswordResetDialog.value){
        resetPasswordDialog(auth)
    }

    Column(Modifier.fillMaxSize().background(color = SoftWhite), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        //
        // Welcome Message
        //
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier
                    .padding(30.dp)
                    .width(500.dp),
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Light,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                text = "Log In")
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
                keyboardType =  KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            isError = !isEmailValid,
            modifier = Modifier
                .fillMaxWidth(.6f)
                .padding(bottom = 15.dp)
        )

        //
        // Password TextField
        //
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(.6f)
                .padding(top = 15.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType =  KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() }
            ),
            isError = !isPasswordValid,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),

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
            modifier = Modifier.padding(vertical = 30.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            //
            // Login Button
            //
            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                Log.d(TAG ,"The user has successfully logged in")
                                /*TODO populate app with info from user data*/
                                isLoggedIn.value = Firebase.auth.currentUser != null
                            }
                            else {
                                Log.w(TAG ,"The user has FAILED to login", it.exception)
                            }
                        }
                },
                modifier = Modifier.size(width = 150.dp, height = 50.dp).padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Aero),
                enabled = isEmailValid && isPasswordValid
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
                    isRegistering.value = true
                },
                enabled = true,
                modifier = Modifier.size(width = 150.dp, height = 50.dp).padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Aero)
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
                fontSize = 18.sp)
        }
    }
}

@Composable
fun resetPasswordDialog(auth: FirebaseAuth){
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
            Column(){
                Row(){
                    Text(
                        text = "Please enter the email address associated with your account.",
                        fontFamily = robotoFamily,
                        color = Color.Black
                    )
                }
                Row(){
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
                if(email.isNotBlank()){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener {
                        if(it.isSuccessful){
                            openPasswordResetDialog.value = false
                            showEmailError = false

                            Toast.makeText(
                                contextForToast,
                                "Password recovery email sent.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else {
                            emailError = "Please enter a valid email."
                            showEmailError = true
                        }
                    }
                } else {
                    emailError = "Please enter a valid email."
                    showEmailError = true
                }
            }){
                Text(
                    text = "CONFIRM",
                    fontFamily = robotoFamily,
                    color = Aero,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                openPasswordResetDialog.value = false
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
