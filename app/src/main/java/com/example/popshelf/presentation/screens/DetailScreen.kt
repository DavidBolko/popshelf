package com.example.popshelf.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.popshelf.R
import com.example.popshelf.presentation.UIEvent
import com.example.popshelf.presentation.UIState
import com.example.popshelf.presentation.components.Rating
import com.example.popshelf.presentation.ValidateState
import com.example.popshelf.presentation.viewmodels.DetailViewModel


/***
 * Composable function representing detail screen of added or searched work.
 * @author David Bolko
 * @param modifier - modifier for ability to change the look of the composable screen from outside
 * @param nav - navigation controller to allow navigation from this screen or to the next.
 * @param detailViewModel - DetailViewmodel, viewmodel for fetching and preserving data for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(modifier: Modifier = Modifier, nav: NavController, detailViewModel: DetailViewModel) {
    val context = LocalContext.current
    val state = detailViewModel.data.collectAsState().value

    val imageLoader = ImageLoader.Builder(context).components { add(GifDecoder.Factory()) }.build()
    val image = ImageRequest.Builder(LocalContext.current).data(R.drawable.placeholder).placeholder(R.drawable.placeholder).build()

    LaunchedEffect(Unit) {
        detailViewModel.state.collect { event ->
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
        topBar = { TopAppBar(title = { Text("Detail") },
            navigationIcon = {
                /*
                Používa sa lambda funkcia dropUnlessResumed, ktorá zabezpeči že sa vložena funkcia
                iba ak je lifecycle stav aspon RESUMED.
                 */
                IconButton(onClick = dropUnlessResumed { nav.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, stringResource(R.string.back))
                }
            },
            actions = {
                if(!detailViewModel.fromShelf){
                    IconButton(onClick = dropUnlessResumed { detailViewModel.onDelete() } ) {
                        Icon(imageVector = Icons.Default.Delete, stringResource(R.string.delete))
                    }
                }
            })
        },
        floatingActionButton = {
            if (state is UIState.Success && detailViewModel.fromShelf) {
                val mediaItem = state.data
                FloatingActionButton(
                    onClick = dropUnlessResumed {
                        nav.navigate("add/${mediaItem.id}/${detailViewModel.mediaType}/${mediaItem.shelfId}/${false}")
                    }
                ) {
                    Icon(Icons.Filled.Add, stringResource(R.string.add_to_collection))
                }
            }else if (state is UIState.Success) {
                val mediaItem = state.data
                FloatingActionButton(
                    onClick = dropUnlessResumed {
                        nav.navigate("add/${mediaItem.id}/${detailViewModel.mediaType}/${mediaItem.shelfId}/${true}")
                    }
                ) {
                    Icon(Icons.Filled.Edit, stringResource(R.string.edit))
                }
            }
        }
    )
    {padding ->
        ValidateState(state, isInternet = detailViewModel.isConnected.collectAsState().value) { item->
            Column(Modifier.padding(padding).padding(vertical = 16.dp, horizontal = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(){
                    Row(modifier = modifier.padding(12.dp)) {
                        AsyncImage(modifier = Modifier.height(200.dp), model = image, contentDescription = "${stringResource(R.string.work_cover)} ${item.title}", imageLoader = imageLoader)
                        Column(modifier = Modifier.padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(item.title, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                            Text(item.author, fontSize = 16.sp)
                            Text(item.publishYear.toString())
                            Rating(item.rating)
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Card() {
                        Column(modifier = modifier.padding(12.dp)){
                            Text(stringResource(R.string.description), fontWeight = FontWeight.Medium)
                            Text(textAlign = TextAlign.Justify, text=item.desc)
                        }
                    }
                    if (!detailViewModel.fromShelf && !item.comment.isNullOrEmpty()) {
                        Card(modifier=modifier.fillMaxWidth()){
                            Column(modifier = modifier.padding(12.dp)) {
                                Text(stringResource(R.string.comment), fontWeight = FontWeight.Medium)
                                Text(textAlign = TextAlign.Justify, text = item.comment)
                            }
                        }
                    }
                }
            }
        }

    }
}

