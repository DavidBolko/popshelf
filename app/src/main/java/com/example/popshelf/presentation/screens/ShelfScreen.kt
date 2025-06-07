package com.example.popshelf.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.example.popshelf.R
import com.example.popshelf.presentation.UIEvent
import com.example.popshelf.presentation.components.MediaItem
import com.example.popshelf.presentation.ValidateState
import com.example.popshelf.presentation.viewmodels.ShelfViewModel

/***
 * Composable function representing shelf screen, it preserves added work to specific shelf.
 * @param modifier - modifier for ability to change the look of the composable screen from outside.
 * @param nav - navigation controller to allow navigation from this screen or to the next.
 * @param shelfViewModel - shelfViewModel, viewmodel for fetching and preserving data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfScreen(modifier: Modifier = Modifier, shelfViewModel: ShelfViewModel, nav: NavController) {
    val uiState by shelfViewModel.data.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        shelfViewModel.state.collect { event ->
            when (event) {
                is UIEvent.NavigateBack -> {
                    nav.navigate("home")
                }
                is UIEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        modifier=modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(shelfViewModel.name) },
                navigationIcon = {
                    IconButton(onClick = dropUnlessResumed { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    if (!shelfViewModel.isSystem) {
                        IconButton(onClick = dropUnlessResumed {
                            shelfViewModel.deleteShelf()
                        }) {
                            Icon(Icons.Default.FolderDelete, contentDescription = stringResource(R.string.delete))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier.padding(paddingValues).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            ValidateState(uiState) { items ->
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(16.dp)) {
                    items(items) { item ->
                        MediaItem(item = item, openDetail = dropUnlessResumed{ nav.navigate("detail/${item.id}/${item.mediaType.name}/${false}") },)
                    }
                }
            }
        }
    }
}