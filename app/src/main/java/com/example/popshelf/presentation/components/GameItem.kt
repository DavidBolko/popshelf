package com.example.popshelf.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.popshelf.data.Game

@Composable
fun GameItem(game: Game) {
    Row(modifier = Modifier.padding(8.dp)){
        AsyncImage(model = game.background_image, contentDescription = null,modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = game.name, maxLines = 1, overflow = TextOverflow.Clip)
        }
    }
}