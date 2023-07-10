package com.example.travelapp

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp.MainActivity.Companion.TAG
import com.example.travelapp.ui.theme.Aero
import com.example.travelapp.ui.theme.TravelAppTheme
import com.google.firebase.auth.FirebaseAuth

val isLoggedIn = mutableStateOf(false)
val isRegistering = mutableStateOf(false)

@Composable
fun Login(auth: FirebaseAuth){
    val focusManager = LocalFocusManager.current

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

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        //
        // Welcome Message
        //
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier
                    .padding(30.dp)
                    .width(500.dp),
                fontSize = 60.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                text = "Welcome to TRAVELYZE")
        }

        //
        // Login Fields
        //
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
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
                    isError = !isEmailValid
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

                //
                // Login Button
                //
                Button(
                    onClick = {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener{
                                if(it.isSuccessful){
                                    Log.d(TAG ,"The user has successfully logged in")
                                    isLoggedIn.value = true
                                    /*TODO populate app with info from user data*/
                                }
                                else {
                                    Log.w(TAG ,"The user has FAILED to login", it.exception)
                                }
                            }
                    },
                    modifier = Modifier
                        .padding(start = 15.dp, top = 20.dp)
                        .size(width = 200.dp, height = 50.dp),
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
            }
        }

        //
        // Forgot Password Button
        //
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){//Forgot Password
            TextButton(
                onClick = { /*TODO Prompt email input to send recovery email*/ }
            ) {
                Text(
                    text = "Forgot password?",
                    color = Color.Black,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(end = 20.dp, top = 10.dp),
                    fontSize = 18.sp)
            }
        }

        //
        // Register Elements
        //
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "New to Travelyze?",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 25.dp, top = 25.dp),
                fontSize = 18.sp
            )
            //
            // Register Button
            //
            Button(
                onClick = {
                    isRegistering.value = true
                },
                enabled = true,
                modifier = Modifier.padding(start = 20.dp, top = 15.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Aero)
            ) {
                Text(
                    text = "Register",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
