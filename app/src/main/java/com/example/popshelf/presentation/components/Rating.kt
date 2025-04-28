package com.example.popshelf.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.popshelf.presentation.screens.calculateRating

@Composable
fun Rating(rating: Int, onChangeRating: ((Int) -> Unit)? = null){
    Card() {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            (1..5).forEach { index ->
                val icon = calculateRating(index, rating)
                val tint = Color(0xFFFFC107)

                if (onChangeRating != null) {
                    IconButton(onClick = { if (rating == index) onChangeRating(0) else onChangeRating(index) }) {
                        Icon(imageVector = icon, contentDescription = "Rating $index", modifier = Modifier.size(32.dp), tint = tint)
                    }
                } else {
                    Icon(imageVector = icon, contentDescription = "Rating $index", modifier = Modifier.size(32.dp), tint = tint)
                }
            }
        }
    }
}
