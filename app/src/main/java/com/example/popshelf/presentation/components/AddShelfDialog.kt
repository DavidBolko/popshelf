package com.example.popshelf.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.popshelf.R
import com.example.popshelf.presentation.viewmodels.AddShelfViewModel


/**
 * Composable function which represents add shelf dialog
 * @param onDismiss onDismiss callback when user request to dismiss the dialog.
 * @param addShelfViewModel viewmodel for fetching and preserving data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShelfDialog(onDismiss: () -> Unit, addShelfViewModel: AddShelfViewModel ) {
    val name by addShelfViewModel.nameState.collectAsState()
    val selectedColorKey by addShelfViewModel.selectedColorKey.collectAsState()
    val isCreationAllowed by addShelfViewModel.isCreationAllowed.collectAsState()
    val selectedColor = colorOptions[selectedColorKey] ?: colorOptions["Pink"]!!
    val colorKeys = colorOptions.keys.toList()

    Dialog(onDismissRequest = onDismiss, properties =DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Surface(modifier = Modifier.fillMaxSize().imePadding()) {
            Column(modifier = Modifier.padding(16.dp)
            ) {
                TopAppBar(
                    title = { Text(stringResource(R.string.add_shelf)) },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close_icon))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = name, onValueChange = { addShelfViewModel.onNameChange(it) }, label = { Text(
                    stringResource(R.string.name)
                ) }, modifier = Modifier.fillMaxWidth(), maxLines = 1)
                Spacer(modifier = Modifier.height(16.dp))

                var expanded by remember { mutableStateOf(false) }

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Box(modifier = Modifier.size(16.dp).background(selectedColor, shape = CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedColorKey)
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        colorKeys.forEach { key ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(16.dp).background(colorOptions[key] ?: Color.Gray, shape = CircleShape))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(key)
                                    }
                                },
                                onClick = {
                                    addShelfViewModel.onColorSelected(key)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { addShelfViewModel.createShelf(); onDismiss() },enabled = isCreationAllowed) {
                        Text(stringResource(R.string.add))
                    }
                }
            }
        }
    }
}
