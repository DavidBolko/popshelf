package com.example.popshelf.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.popshelf.R

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context).components { add(GifDecoder.Factory()) }.build()

    val request = ImageRequest.Builder(context).data(R.drawable.no_books).build()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(modifier = Modifier.fillMaxWidth().height(100.dp), model = request, contentDescription = "Žiadne pridane knižky", imageLoader = imageLoader)
            Text("No books were added yet...")
        }
    }
}