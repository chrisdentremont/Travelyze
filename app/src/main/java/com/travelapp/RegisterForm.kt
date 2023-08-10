package com.travelapp

import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.travelapp.composable.*
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.TextButtonColor
import com.travelapp.ui.theme.robotoFamily

/**
 * The main method for creating the register form when creating a new account
 */
@Composable
fun RegisterForm() {
    val auth by lazy {
        Firebase.auth
    }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    //Info variables
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //Variables to ensure the info variables are formatted properly
    var validateFirstName by rememberSaveable { mutableStateOf(true) }
    var validateLastName by rememberSaveable { mutableStateOf(true) }
    var validateEmail by rememberSaveable { mutableStateOf(true) }
    var validatePassword by rememberSaveable { mutableStateOf(true) }
    var validateConfirmPassword by rememberSaveable { mutableStateOf(true) }
    var validateUserName by rememberSaveable { mutableStateOf(true) }
    var validateArePasswordsEqual by rememberSaveable { mutableStateOf(true) }
    var isPasswordVisible by rememberSaveable() { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable() { mutableStateOf(false) }

    //Error messages
    val validateFirstNameError = "Please input a valid first name"
    val validateLastNameError = "Please input a valid last name"
    val validateEmailError = "Please input a valid email"
    val validateUserNameError = "Please input an appropriate username"
    val validatePasswordError = "Password must mix capital and non-capital letters, a number, special character, and a minimum length of 8"
    val validateEqualPasswordError = "Passwords must be equal"

    /**
     * Method for validating the entered data and ensuring it fits a format or that something was entered
     *
     * @param firstname The user's first name
     * @param lastname The user's last name
     * @param email The user's email
     * @param username The user's username
     * @param password The user's password (this is not saved anywhere else)
     * @param confirmPassword The user's password again, ensuring it was entered correctly
     *
     * @return Returns whether all the data is satisfactory or not
     */
    fun validateData(
        firstname: String,
        lastname: String,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
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

    /**
     * The method that creates a user's account once the information they entered is valid and they
     * push the register button
     *
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param email The user's email
     * @param userName The user's username
     * @param password The user's password (this is not saved anywhere else)
     * @param confirmPassword The user's password again, ensuring it was entered correctly
     */
    fun register(
        firstName: String,
        lastName: String,
        email: String,
        userName: String,
        password: String,
        confirmPassword: String
    ) {
        val fireStore = FirebaseFirestore.getInstance()
        val storage = Firebase.storage

        if (validateData(firstName, lastName, email, userName, password, confirmPassword)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {

                        val newUser = task.result.user
                        newUser!!.updateProfile(userProfileChangeRequest {
                            displayName = userName
                            photoUri =
                                Uri.parse("android.resource://com.travelapp/" + R.drawable.default_profile_picture)
                        })

                        val defaultPic =
                            Uri.parse("android.resource://com.travelapp/" + R.drawable.default_profile_picture.toString())
                        val profilePicRef =
                            storage.reference.child("users/${newUser.uid}/profile_picture.jpg")
                        profilePicRef.putFile(defaultPic)


                        val user = TravelyzeUser(
                            AccountInfo(
                                firstName,
                                lastName,
                                userName,
                                email
                            ),

                            AccountData(
                                mutableListOf(),
                                mutableListOf()
                            ),

                            AccountRequests(
                                mutableListOf(),
                                mutableListOf()
                            )
                        )

                        val userID = auth.currentUser?.uid.toString()
                        val documentReference = fireStore.collection("users").document(userID)

                        documentReference.set(user)

                        Toast.makeText(context, "Account Successfully Created!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Error creating account.", Toast.LENGTH_SHORT)
                            .show()
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
            .background(BackgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Create account",
            modifier = Modifier.padding(bottom = 20.dp),
            color = Color.Black,
            fontFamily = robotoFamily,
            fontWeight = FontWeight.Light,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        /**
         * First Name Text Field
         */
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
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp),
            isDialog = false
        )

        /**
         * Last Name Text Field
         */
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
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp),
            isDialog = false
        )

        /**
         * Email Text Field
         */
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
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp),
            isDialog = false
        )

        /**
         * Username Text Field
         */
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
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp),
            isDialog = false
        )

        /**
         * Password Text Field
         */
        CustomOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            showError = !validatePassword,
            errorMessage = validatePasswordError,
            isPasswordField = true,
            isPasswordVisible = isPasswordVisible,
            onVisibilityChange = { isPasswordVisible = it },
            leadingIconImageVector = Icons.Default.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp),
            isDialog = false
        )

        /**
         * Confirm Password Text Field
         */
        CustomOutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirm Password",
            showError = !validateConfirmPassword || !validateArePasswordsEqual,
            errorMessage = if (!validateConfirmPassword) validatePasswordError else validateEqualPasswordError,
            isPasswordField = true,
            isPasswordVisible = isConfirmPasswordVisible,
            onVisibilityChange = { isConfirmPasswordVisible = it },
            leadingIconImageVector = Icons.Default.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(bottom = 10.dp),
            isDialog = false
        )

        /**
         * Register Button
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    register(firstname, lastname, email, username, password, confirmPassword)
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 10.dp, bottom = 30.dp)
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.3f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = TextButtonColor,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Register", fontSize = 20.sp)
            }
        }

    }
}