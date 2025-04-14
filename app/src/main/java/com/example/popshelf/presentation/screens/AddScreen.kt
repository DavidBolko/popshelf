package com.example.popshelf.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.popshelf.presentation.MediaStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(modifier: Modifier = Modifier, nav:NavController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(MediaStatus.FINISHED) }
    var rating by remember { mutableIntStateOf(2) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add to collection") }, navigationIcon = {
            IconButton(onClick = { nav.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Späť"
                )
            }
        }) },
        floatingActionButton = { FloatingActionButton(onClick = {}) { Icon(Icons.Filled.Check, "Add to collection") } }
    )
    {padding ->
        Column(Modifier.padding(padding).padding(vertical = 16.dp, horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row() {
                Column(modifier = Modifier.padding(horizontal = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("The Witcher", fontSize = 24.sp, fontWeight = FontWeight.Medium)
                    Text("J.R.R Tolkien", fontSize = 16.sp)
                    Text("1945")
                }
            }
            Row() {
                ExposedDropdownMenuBox(expanded=expanded, onExpandedChange = {expanded=false}, modifier=Modifier.fillMaxWidth()){
                    TextField(
                        value = selectedStatus.title,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Stav") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier=Modifier.fillMaxWidth()) {
                        DropdownMenuItem(
                            text = { Text(MediaStatus.ONGOING.title) },
                            onClick = { selectedStatus = MediaStatus.ONGOING }
                        )
                        DropdownMenuItem(
                            text = { Text(MediaStatus.PLANNED.title) },
                            onClick = { selectedStatus = MediaStatus.PLANNED }
                        )
                        DropdownMenuItem(
                            text = { Text(MediaStatus.FINISHED.title) },
                            onClick = { selectedStatus = MediaStatus.FINISHED }
                        )
                    }
                }
            }
            if(selectedStatus == MediaStatus.FINISHED){
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    IconButton(onClick = {rating=1}) { Icon(imageVector = calculateRating(1, rating), contentDescription = "Rating 1", modifier = Modifier.size(32.dp)) }
                    IconButton(onClick = {rating=2}) { Icon(imageVector = calculateRating(2, rating), contentDescription = "Rating 1", modifier = Modifier.size(32.dp)) }
                    IconButton(onClick = {rating=3}) { Icon(imageVector = calculateRating(3, rating), contentDescription = "Rating 1", modifier = Modifier.size(32.dp)) }
                    IconButton(onClick = {rating=4}) { Icon(imageVector = calculateRating(4, rating), contentDescription = "Rating 1", modifier = Modifier.size(32.dp)) }
                    IconButton(onClick = {rating=5}) { Icon(imageVector = calculateRating(5, rating), contentDescription = "Rating 1", modifier = Modifier.size(32.dp))}
                }
            }
        }

    }
}


fun calculateRating(level: Int, rating: Int): ImageVector{
    var outlined = Icons.Default.StarOutline
    var filled = Icons.Default.Star
    if(level <= rating){
        return filled
    }
    return outlined
}

