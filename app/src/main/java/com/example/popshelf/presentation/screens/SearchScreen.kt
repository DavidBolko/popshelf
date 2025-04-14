package com.example.popshelf.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.popshelf.domain.MediaItem
import com.example.popshelf.presentation.UIState
import com.example.popshelf.presentation.components.MediaItem
import com.example.popshelf.presentation.viewmodels.SearchViewModel
import com.example.popshelf.presentation.viewmodels.factories.SearchViewModelFactory

@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: SearchViewModel, nav:NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val tabTitles = listOf("books", "games", "movies")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier) {
        Column(modifier= Modifier){
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query -> viewModel.updateSearchQuery(query, selectedTabIndex)},
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape= RoundedCornerShape(50),
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "asdas") },
                placeholder = { Text("Search...") }
            )

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index; viewModel.updateSearchQuery(searchQuery, selectedTabIndex)},
                        text = { Text(title) }
                    )
                }
            }
        }

        when (uiState) {
            is UIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UIState.Success -> {
                renderItems((uiState as UIState.Success).data, nav)
                /*
                when(selectedTabIndex){
                    0-> renderBooks((uiState as UIState.Success).data)
                    1-> renderGames((uiState as UIState.Success).data)
                }

                 */
            }

            is UIState.Error -> {
                val message = (uiState as UIState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Chyba: $message", color = Color.Red, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun renderItems(items: List<Any>, nav:NavController){
    val items = items.filterIsInstance<MediaItem>()
    LazyColumn {
        items(items) { item ->
            MediaItem(item, openDetail = { nav.navigate("detail/${item.id}") })
        }
    }
}

/*
@Composable
fun renderBooks(items: List<Any>){
    val books = items.filterIsInstance<Book>()
    LazyColumn {
        items(books) { book ->
            BookItem(book)
        }
    }
}
@Composable
fun renderGames(items: List<Any>){
    val games = items.filterIsInstance<Game>()

    if (games.isNotEmpty()) {
        LazyColumn {
            items(games) { game ->
                GameItem(game)
            }
        }
    } else {
        Text("Žiadne hry sa nenašli.")
    }
}
*/
