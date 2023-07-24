package com.example.travelapp


import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp.composable.AccountData
import com.example.travelapp.composable.AccountInfo
import com.example.travelapp.composable.CustomOutlinedTextField
import com.example.travelapp.composable.TravelyzeUser
import com.example.travelapp.ui.theme.Aero
import com.example.travelapp.ui.theme.SoftWhite
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

val showNavBar = mutableStateOf(true)

@Composable
fun RegisterForm(){
    val auth by lazy {
        Firebase.auth
    }

    showNavBar.value = false

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var firstname by rememberSaveable {mutableStateOf("")}
    var lastname by rememberSaveable {mutableStateOf("")}
    var email by rememberSaveable {mutableStateOf("")}
    var username by rememberSaveable {mutableStateOf("")}
    var password by rememberSaveable {mutableStateOf("")}
    var confirmPassword by rememberSaveable {mutableStateOf("")}

    var validateFirstName by rememberSaveable { mutableStateOf(true) }
    var validateLastName by rememberSaveable { mutableStateOf(true) }
    var validateEmail by rememberSaveable { mutableStateOf(true) }
    var validatePassword by rememberSaveable { mutableStateOf(true) }
    var validateConfirmPassword by rememberSaveable { mutableStateOf(true) }
    var validateUserName by rememberSaveable { mutableStateOf(true) }
    var validateArePasswordsEqual by rememberSaveable { mutableStateOf(true) }
    var isPasswordVisible by rememberSaveable() { mutableStateOf(true) }
    var isConfirmPasswordVisible by rememberSaveable() { mutableStateOf(true) }

    val validateFirstNameError = "Please input a valid first name"
    val validateLastNameError = "Please input a valid last name"
    val validateEmailError = "Please input a valid email"
    val validateUserNameError = "Please input an appropriate username"
    val validatePasswordError = "Password must mix capital and non-capital letters, a number, special character, and a minimum length of 8"
    val validateEqualPasswordError = "Passwords must be equal"

    fun validateData(firstname: String, lastname: String, email:String, username: String, password: String, confirmPassword: String): Boolean {
        val passwordRegex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,}".toRegex()

        validateFirstName = firstname.isNotBlank()
        validateLastName = lastname.isNotBlank()
        validateEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        validateUserName = username.isNotBlank()
        validatePassword = passwordRegex.matches(password)
        validateConfirmPassword = passwordRegex.matches(confirmPassword)
        validateArePasswordsEqual = password == confirmPassword

        return validateFirstName && validateLastName && validateEmail && validatePassword && validateConfirmPassword && validateArePasswordsEqual
    }

    fun register (
        firstName: String,
        lastName: String,
        email: String,
        userName: String,
        password: String,
        confirmPassword: String
    ){
        val fireStore = FirebaseFirestore.getInstance()

        if(validateData(firstName, lastName, email, userName, password, confirmPassword)){
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(){ task ->
                    if(task.isSuccessful){

                        var user = TravelyzeUser (
                            AccountInfo(
                                firstName,
                                lastName,
                                userName,
                                email
                            ),

                            AccountData(
                                mutableListOf(),
                                mutableListOf()
                            )
                        )

                        var userID = auth.currentUser?.uid.toString()
                        var documentReference = fireStore.collection("users").document(userID)

                        documentReference.set(user)

                        isRegistering.value = false
                        Toast.makeText(context, "Account Successfully Created!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Error creating account.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Please review fields", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(SoftWhite)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Register an account on Travelyze",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 20.dp),
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        //
        // First Name
        //
        CustomOutlinedTextField(
            value = firstname,
            onValueChange = { firstname = it },
            label = "First Name",
            showError = !validateFirstName,
            errorMessage = validateFirstNameError,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp)
        )

        //
        // Last Name
        //
        CustomOutlinedTextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = "Last Name",
            showError = !validateLastName,
            errorMessage = validateLastNameError,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)}
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp)
        )

        //
        // Email Address
        //
        CustomOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            showError = !validateEmail,
            errorMessage = validateEmailError,
            leadingIconImageVector = Icons.Default.AlternateEmail,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)}
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp)
        )

        //
        // Username
        //
        CustomOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            showError = !validateUserName,
            errorMessage = validateUserNameError,
            leadingIconImageVector = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)}
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp)
        )

        //
        // Password
        //
        CustomOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            showError = !validatePassword,
            errorMessage = validatePasswordError,
            isPasswordField = true,
            isPasswordVisible = isPasswordVisible,
            onVisibilityChange = {isPasswordVisible = it},
            leadingIconImageVector = Icons.Default.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)}
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp)
        )

        //
        // Confirm Password
        //
        CustomOutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            showError = !validateConfirmPassword || !validateArePasswordsEqual,
            errorMessage = if(!validateConfirmPassword) validatePasswordError else validateEqualPasswordError,
            isPasswordField = true,
            isPasswordVisible = isConfirmPasswordVisible,
            onVisibilityChange = {isConfirmPasswordVisible = it},
            leadingIconImageVector = Icons.Default.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    register(firstname, lastname, email, username, password, confirmPassword)
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 10.dp, bottom = 30.dp)
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.3f),
                colors = ButtonDefaults.buttonColors(backgroundColor = Aero, contentColor = Color.White)
            ){
                Text(text = "Register", fontSize = 20.sp)
            }

            Button(
                onClick = {
                    isRegistering.value = false
                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 20.dp, bottom = 30.dp)
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.3f),
                colors = ButtonDefaults.buttonColors(backgroundColor = Aero, contentColor = Color.White),

            ){
                Text(text = "Cancel", fontSize = 20.sp)
            }
        }

    }
}