package com.example.travelapp.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp.openDeleteDialog
import com.example.travelapp.openSignoutDialog
import com.example.travelapp.sendEmailToExistingUser
import com.example.travelapp.sendPasswordChangeEmail
import com.example.travelapp.ui.theme.Aero
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    leadingIconImageVector: ImageVector,
    leadingIconDescription: String = "",
    isPasswordField: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showError: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it)},
            modifier = modifier,
            label = {Text(label)},
            leadingIcon = {
                Icon(
                    imageVector = leadingIconImageVector,
                    contentDescription = leadingIconDescription,
                    tint = if (showError) MaterialTheme.colors.error else MaterialTheme.colors.onSurface
                )
            },
            isError = showError,
            trailingIcon = {
                if(showError && !isPasswordField) Icon(imageVector = Icons.Filled.Error, contentDescription = "Show error icon")
                if(isPasswordField){
                    IconButton(onClick = {onVisibilityChange(!isPasswordVisible)}) {
                        Icon(
                            imageVector = if(isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            visualTransformation = when {
                isPasswordField && isPasswordVisible -> VisualTransformation.None
                isPasswordField -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true
        )

        if(showError){
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .offset(y = (-8).dp)
                    .fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
fun TopBar(title: String = "", buttonIcon: ImageVector, onButtonClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
            )
        },
        actions = {
            IconButton(
                onClick = { onButtonClicked() },
            ) {
                Icon(
                    buttonIcon,
                    contentDescription = "",
                    modifier = Modifier.size(size = 40.dp)
                )
            }
        },
        backgroundColor = Aero
    )
}

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    auth: FirebaseAuth
) {
    Column(
        modifier
            .height(1000.dp)
            .padding(top = 50.dp)
    ) {
        TextButton(
            onClick = {
                /*TODO Prompt username change window*/
                //TODO Figure out how to read data once its in the database
                var fireStore = FirebaseFirestore.getInstance()
                var users = mutableListOf<TravelyzeUser?>()

                fireStore.collection("users").get()
                    .addOnSuccessListener {
                        queryDocumentSnapshots ->

                        if(!queryDocumentSnapshots.isEmpty) {
                            val list = queryDocumentSnapshots.documents
                            for(d in list) {
                                val u: TravelyzeUser? = d.toObject(TravelyzeUser::class.java)

                                users.add(u)
                            }
                        }
                    }

                var userID = auth.currentUser?.uid.toString()
                var documentReference = fireStore.collection("users").document(userID)

//                var user = TravelyzeUser (
//                    AccountInfo(
//                        firstName,
//                        lastName,
//                        userName,
//                        email
//                    ),
//
//                    AccountData(
//                        mutableListOf(),
//                        mutableListOf()
//                    )
//                )
//
//                var userAccount = mutableMapOf<String, TravelyzeUser>()
//                userAccount["UserAccount"] = user
//
//                documentReference.set(userAccount)
            },
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ){  }
        ) {
            Text(
                text = "Edit Username",
                color = Color.Black,
                modifier = Modifier.padding(start = 15.dp),
                fontSize = 30.sp)
        }

        TextButton(
            onClick = { sendPasswordChangeEmail.value = true }
        ) {
            Text(
                text = "Change password",
                color = Color.Black,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                fontSize = 30.sp)
        }

        TextButton(
            onClick = {
                openSignoutDialog.value = true
            }
        ) {
            Text(
                text = "Sign out",
                color = Color.Blue,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp),
                fontSize = 30.sp)
        }
        Divider(startIndent = 0.dp, thickness = 2.dp, color = Color.Black)

        TextButton(
            onClick = {
                openDeleteDialog.value = true
            }
        ) {
            Text(
                text = "Delete Account",
                color = Color.Red,
                modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                fontSize = 30.sp)
        }


    }
}

