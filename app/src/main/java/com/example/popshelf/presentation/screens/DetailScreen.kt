package com.example.popshelf.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.popshelf.R
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.UIState
import com.example.popshelf.presentation.components.Rating
import com.example.popshelf.presentation.validateState
import com.example.popshelf.presentation.viewmodels.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(modifier: Modifier = Modifier, nav: NavController, viewModel: DetailViewModel) {
    val context = LocalContext.current
    val state = viewModel.data.collectAsState().value

    val imageLoader = ImageLoader.Builder(context).components { add(GifDecoder.Factory()) }.build()
    val image = ImageRequest.Builder(LocalContext.current).data(R.drawable.placeholder).placeholder(R.drawable.placeholder).build()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detail") }, navigationIcon = {
            IconButton(onClick = { nav.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Späť")
            }
        }) },
        floatingActionButton = {
            if (state is UIState.Success && viewModel.fromShelf) {
                val mediaItem = state.data
                FloatingActionButton(
                    onClick = {
                        nav.navigate("add/${mediaItem.id}/${viewModel.mediaType}")
                    }
                ) {
                    Icon(Icons.Filled.Add, "Add to collection")
                }
            }
        }
    )
    {padding ->
        validateState(state) {item->
            Column(Modifier.padding(padding).padding(vertical = 16.dp, horizontal = 24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row() {
                    AsyncImage(modifier = Modifier.height(200.dp), model = image, contentDescription = "Žiadne pridane knižky", imageLoader = imageLoader)
                    Column(modifier = Modifier.padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(item.title, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                        Text(item.author, fontSize = 16.sp)
                        Text(item.publishYear.toString())
                        Rating(item.rating)
                    }
                }
                Column() {
                    Text("Description", fontWeight = FontWeight.Medium)
                    Text(textAlign = TextAlign.Justify, text=item.desc)
                }
            }
        }

    }
}

