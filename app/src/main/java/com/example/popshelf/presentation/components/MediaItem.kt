package com.example.popshelf.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.MediaType


/**
 * Composable function which renders individual media work items in shelves.
 * @param item instance of data class MediaItem, which holds all the information which is going to be displayed.
 * @param openDetail lambda function which tells this component what should be done when opening a detail of item.
 * @param mediaType media type of item defined by [MediaType]
 */
@Composable
fun MediaItem(item: MediaItem, openDetail: ()->Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp, 8.dp, 8.dp, bottom = 2.dp).clickable { openDetail() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
    )){
        Row(modifier = Modifier.padding(8.dp)){
            val image = when(item.mediaType){
                MediaType.MOVIES -> item.cover
                MediaType.GAMES -> "https://images.igdb.com/igdb/image/upload/t_cover_big/${item.cover}.jpg"
                MediaType.BOOKS -> "https://covers.openlibrary.org/b/id/${item.cover}-S.jpg"
            } //real obrazok z API
            //val image = ImageRequest.Builder(LocalContext.current).data(R.drawable.placeholder).placeholder(R.drawable.placeholder).build()
            AsyncImage(model = image, contentDescription = null, modifier = Modifier.size(80.dp))

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = item.title, maxLines = 1, overflow = TextOverflow.Clip)
                if (item.author.isNotBlank() && !item.author.split(",").all { it.trim().toIntOrNull() != null }) {
                    Text(text = item.author, fontSize = 15.sp, fontWeight = FontWeight.Thin)
                }
                Text(text = item.released.toString(), fontSize = 10.sp, fontWeight = FontWeight.Thin)
            }
        }
    }
}