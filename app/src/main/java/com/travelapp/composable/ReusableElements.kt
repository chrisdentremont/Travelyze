package com.travelapp.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.travelapp.locationSelected
import com.travelapp.selectedName
import com.travelapp.ui.theme.BackgroundAccentColor
import com.travelapp.ui.theme.TextButtonColor
import com.travelapp.ui.theme.robotoFamily

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
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = modifier,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIconImageVector,
                    contentDescription = leadingIconDescription,
                    tint = if (showError) MaterialTheme.colors.error else if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
            },
            isError = showError,
            trailingIcon = {
                if (showError && !isPasswordField) Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Show error icon"
                )
                if (isPasswordField) {
                    IconButton(onClick = { onVisibilityChange(!isPasswordVisible) }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
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
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = TextButtonColor,
                cursorColor = TextButtonColor,
                focusedLabelColor = TextButtonColor,

                unfocusedBorderColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                textColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                unfocusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                leadingIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black),
        )

        if (showError) {
            Text(
                text = errorMessage,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
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
                fontSize = 25.sp,
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
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
        backgroundColor = BackgroundAccentColor
    )
}