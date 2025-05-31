package com.example.popshelf.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.example.popshelf.R
import com.example.popshelf.presentation.MediaStatus
import com.example.popshelf.presentation.UIEvent
import com.example.popshelf.presentation.components.Rating
import com.example.popshelf.presentation.ValidateState
import com.example.popshelf.presentation.viewmodels.AddEditItemViewModel


/***
 * Composable function representing add and edit screen of added or searched work.
 * @author David Bolko
 * @param modifier - modifier for ability to change the look of the composable screen from outside
 * @param nav - navigation controller to allow navigation from this screen or to the next.
 * @param addEditItemViewModel - AddEditItemViewmodel, viewmodel for fetching and preserving data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(modifier: Modifier = Modifier, nav:NavController, addEditItemViewModel: AddEditItemViewModel) {
    val media by addEditItemViewModel.mediaItemState.collectAsState()
    var expandedStatus by remember { mutableStateOf(false) }
    var expandedShelf by remember { mutableStateOf(false) }
    val selectedStatus by addEditItemViewModel.selectedStatus.collectAsState()
    val selectedShelf by addEditItemViewModel.selectedShelf.collectAsState()
    val comment by addEditItemViewModel.comment.collectAsState()
    val rating by addEditItemViewModel.rating.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        addEditItemViewModel.state.collect { event ->
            when (event) {
                is UIEvent.NavigateBack -> {
                    nav.navigate("home")
                }
                is UIEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                UIEvent.Idle -> TODO()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.add_to_collection)) }, navigationIcon = {
            IconButton(onClick = dropUnlessResumed { nav.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        }) },
        floatingActionButton = { FloatingActionButton(onClick = dropUnlessResumed {addEditItemViewModel.onConfirm()}) { Icon(Icons.Filled.Check, R.string.add_to_collection.toString()) } }
    )
    {padding ->
        ValidateState(media, isInternet = addEditItemViewModel.isConnected.collectAsState().value) { item ->
            Column(Modifier.padding(padding).padding(vertical = 16.dp, horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row() {
                    Column(modifier = Modifier.padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(item.title, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                        Text(item.author, fontSize = 16.sp)
                        Text(item.publishYear.toString())
                    }
                }
                Column() {
                    ExposedDropdownMenuBox(expanded = expandedStatus, onExpandedChange = { expandedStatus = !expandedStatus }, modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = selectedStatus.title,
                            onValueChange = {},
                            label = { Text("Stav") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            readOnly = true
                        )
                        ExposedDropdownMenu(expanded = expandedStatus, onDismissRequest = { expandedStatus = false }, modifier = Modifier.fillMaxWidth()) {
                            MediaStatus.entries.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status.title) },
                                    onClick = dropUnlessResumed {
                                        addEditItemViewModel.onStatusSelected(status)
                                        expandedStatus = false
                                    }
                                )
                            }
                        }
                    }
                    ExposedDropdownMenuBox(expanded = expandedShelf, onExpandedChange = { expandedShelf = !expandedShelf }, modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = selectedShelf,
                            onValueChange = {},
                            label = { Text("Shelf") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedShelf) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            readOnly = true
                        )
                        ExposedDropdownMenu(expanded = expandedShelf, onDismissRequest = { expandedShelf = false }, modifier = Modifier.fillMaxWidth()) {
                            addEditItemViewModel.shelves.forEach { shelf ->
                                DropdownMenuItem(
                                    text = { Text(shelf.name) },
                                    onClick = dropUnlessResumed {
                                        addEditItemViewModel.onShelfSelected(shelf.name)
                                        expandedShelf = false
                                    }
                                )
                            }
                        }
                    }
                }
                if(selectedStatus == MediaStatus.FINISHED){
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.your_rating))
                        Rating(rating, onChangeRating = {addEditItemViewModel.onRatingSelected(it)})
                        Text(stringResource(R.string.comment))
                        TextField(value = comment, onValueChange = {addEditItemViewModel.onCommentChanged(it)}, modifier = Modifier.fillMaxWidth().fillMaxHeight())
                    }

                }
            }
        }

    }
}


fun calculateRating(level: Int, rating: Int): ImageVector{
    var outlined = Icons.Default.StarOutline
    var filled = Icons.Default.Star
    if(level <= rating){
        return filled
    }
    return outlined
}

