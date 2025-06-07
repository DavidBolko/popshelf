package com.example.popshelf.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.dropUnlessResumed
import com.example.popshelf.R
import com.example.popshelf.presentation.components.AddShelfDialog
import com.example.popshelf.presentation.components.ShelfCard
import com.example.popshelf.presentation.ValidateState
import com.example.popshelf.presentation.viewmodels.AddShelfViewModel
import com.example.popshelf.presentation.viewmodels.HomeViewModel


/***
 * Composable function representing home screen of the application, it shows system/default and user
 * created shelves.
 * @param modifier - modifier for ability to change the look of the composable screen from outside
 * @param nav - navigation controller to allow navigation from this screen or to the next.
 * @param addShelfViewModel - addShelfViewModel, viewmodel for feature of adding a shelf, this
 * viewmodel is passed to non screen function, to a "Dialog component" composable.
 * @param homeViewModel - AddEditItemViewmodel, viewmodel for fetching and preserving data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, nav: NavController, addShelfViewModel: AddShelfViewModel, homeViewModel: HomeViewModel) {
    val data by homeViewModel.state.collectAsState()
    val showDialog by homeViewModel.addShelfDialogState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                homeViewModel.refresh()
            }
        }

        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.library)) })},
        floatingActionButton = {
            FloatingActionButton(onClick = dropUnlessResumed { homeViewModel.onDialogClick(true) }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_shelf_button))
            }
        }
    ) { paddingValues ->
        ValidateState(data, isInternet = true) { shelves ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(16.dp)) {
                itemsIndexed(shelves) { index, shelf ->
                    ShelfCard(shelf, nav)

                    if (index == 2) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.your_shelves))
                        HorizontalDivider()
                    }
                }
            }
        }
        if (showDialog) {
            AddShelfDialog(onDismiss = dropUnlessResumed { homeViewModel.onDialogClick(false); homeViewModel.refresh()}, addShelfViewModel = addShelfViewModel)
        }
    }
}