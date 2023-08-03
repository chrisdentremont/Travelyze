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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.travelapp.ui.theme.BackgroundAccentColor
import com.travelapp.ui.theme.TextButtonColor
import com.travelapp.ui.theme.robotoFamily

/**
 * A custom [OutlinedTextField] which allows for more flexible in different scenarios
 *
 * @param value The default value of the [OutlinedTextField]
 * @param onValueChange The action that occurs when the value within the [OutlinedTextField] changes
 * @param label The default label that displays when nothing is typed in
 * @param leadingIconImageVector The icon that appears on the left side of the [OutlinedTextField]
 * @param leadingIconDescription A description of the [leadingIconImageVector]
 * @param isPasswordField A boolean for whether the [OutlinedTextField] will be used for entering a password or not
 * @param isPasswordVisible A boolean for if the password should be displayed by default
 * @param onVisibilityChange The actions that occurs when [isPasswordVisible] is changed
 * @param keyboardOptions keyboard options for the [OutlinedTextField]
 * @param keyboardActions keyboard actions for the [OutlinedTextField]
 * @param showError A boolean for if an error message should be displayed on the [OutlinedTextField]
 * @param errorMessage The message that displays in red under the [OutlinedTextField] when [showError] is true
 * @param modifier Visual modifiers for the [OutlinedTextField]
 */
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

/**
 * The bar that appears at the top of a page, created to keep them uniform
 * in appearance but flexible in content
 *
 * @param title The title of the page that appears on the left side of the bar
 * @param buttonIcon The icon for the button that appears on the right side of the bar
 * @param onButtonClicked The action the button triggers when it is clicked
 */
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