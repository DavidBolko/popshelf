package com.example.popshelf.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.popshelf.R
import com.example.popshelf.data.Book
import com.example.popshelf.domain.MediaItem

@Composable
fun MediaItem(item: MediaItem, openDetail: ()->Unit) {
    Row(modifier = Modifier.padding(8.dp).clickable { openDetail() }){
        //val image = "https://covers.openlibrary.org/b/id/${book.cover_i}-S.jpg", //real obrazok z API
        val image = ImageRequest.Builder(LocalContext.current).data(R.drawable.placeholder).placeholder(R.drawable.placeholder).build()
        AsyncImage(model = image, contentDescription = null, modifier = Modifier.size(80.dp))

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = item.title, maxLines = 1, overflow = TextOverflow.Clip)
            Text(text = item.author, fontSize = 15.sp, fontWeight = FontWeight.Thin)
            Text(text = item.publishYear.toString(), fontSize = 10.sp, fontWeight = FontWeight.Thin)
        }
    }
}