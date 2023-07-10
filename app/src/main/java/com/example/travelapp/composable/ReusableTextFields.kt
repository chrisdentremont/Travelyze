package com.example.travelapp.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

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
    errorMessage: String = ""
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
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