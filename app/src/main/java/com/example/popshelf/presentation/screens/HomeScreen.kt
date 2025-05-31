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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.popshelf.R
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.dropUnlessResumed
import com.example.popshelf.presentation.components.AddShelfDialog
import com.example.popshelf.presentation.components.ShelfCard
import com.example.popshelf.presentation.ValidateState
import com.example.popshelf.presentation.viewmodels.AddShelfViewModel
import com.example.popshelf.presentation.viewmodels.HomeViewModel


/***
 * Composable function representing home screen of the application, it shows system/default and user
 * created shelves.
 * @author David Bolko
 * @param modifier - modifier for ability to change the look of the composable screen from outside
 * @param nav - navigation controller to allow navigation from this screen or to the next.
 * @param addShelfViewModel - addShelfViewModel, viewmodel for feature of adding a shelf, this
 * viewmodel is passed to non screen function, to a "Dialog component" composable.
 * @param homeViewModel - AddEditItemViewmodel, viewmodel for fetching and preserving data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, nav: NavController, addShelfViewModel: AddShelfViewModel, homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val state by homeViewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

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

    val imageLoader = remember { ImageLoader.Builder(context).components { add(GifDecoder.Factory()) }.build() }
    val placeholderRequest = ImageRequest.Builder(context).data(R.drawable.no_books).build()

    Scaffold(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        topBar = { TopAppBar(title = { Text("My Library") })},
        floatingActionButton = {
            FloatingActionButton(onClick = dropUnlessResumed { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Shelf")
            }
        }
    ) { paddingValues ->
        ValidateState(state, isInternet = true) { shelves ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(16.dp)) {
                itemsIndexed(shelves) { index, shelf ->
                    ShelfCard(shelf, nav)

                    if (index == 2) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Your shelves:",)
                        HorizontalDivider()
                    }
                }
            }
        }
        if (showDialog) {
            AddShelfDialog(onDismiss = dropUnlessResumed { showDialog = false; homeViewModel.refresh()}, addShelfViewModel = addShelfViewModel)
        }
    }
}