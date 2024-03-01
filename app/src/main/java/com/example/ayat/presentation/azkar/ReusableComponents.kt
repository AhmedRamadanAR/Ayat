package com.example.ayat.presentation.azkar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReusableDialog(
    titleText: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    textFieldLabel: String? = null,
    textFieldValue: MutableState<String>? = null
) {
    AlertDialog(
        title = { Text(text = titleText) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            Button(modifier= Modifier.padding(horizontal=20.dp),onClick = { onDismiss() }) {
                Text(text = dismissText)
            }
        },
        text = {
            if (textFieldLabel != null && textFieldValue != null) {
                TextField(
                    value = textFieldValue.value,
                    onValueChange = { textFieldValue.value = it },
                    label = { Text(textFieldLabel) }
                )
            }
        }
    )
}