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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.popshelf.R
import com.example.popshelf.domain.Shelf
import java.io.File



/**
 * Composable function which renders rating of media work with stars.
 * @author David Bolko
 * @param shelf instance of data class Shelf, which holds all the information about shelf which is going to be displayed.
 * @param nav navigation controller to allow navigation from this screen or to the next.
 */
@Composable
fun ShelfCard(shelf: Shelf, nav: NavController) {
    val context = LocalContext.current

    val imageData = remember(shelf.image) {
        if (shelf.image == null) {
            null
        } else {
            val name = shelf.image
            val drawableId = context.resources.getIdentifier(name, "drawable", context.packageName)
            if (drawableId != 0) {
                drawableId
            } else {
                val file = File(context.filesDir, "$name.jpg")
                if (file.exists()) file else null
            }
        }
    }


    Card(modifier = Modifier.fillMaxWidth().padding(4.dp).clickable(onClick = dropUnlessResumed { nav.navigate("shelf/${shelf.id}/${shelf.name}") } ) ,
        colors = CardDefaults.cardColors(containerColor = colorOptions[shelf.color] ?: MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            if (imageData != null) {
                val request = ImageRequest.Builder(context).data(imageData).placeholder(R.drawable.placeholder).build()
                AsyncImage(model = request, contentDescription = null, modifier = Modifier.size(60.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column {
                Text(text = shelf.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = "${shelf.itemCount} items", fontSize = 12.sp)
            }
        }
    }
}