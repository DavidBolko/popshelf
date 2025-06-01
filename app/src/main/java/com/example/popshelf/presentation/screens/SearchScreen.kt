package com.example.popshelf.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import com.example.popshelf.R
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.components.Image
import com.example.popshelf.presentation.components.MediaItem
import com.example.popshelf.presentation.ValidateState
import com.example.popshelf.presentation.viewmodels.SearchViewModel


/***
 * Composable function representing search screen of the application, it allows a user to search works.
 * @author David Bolko
 * @param modifier - modifier for ability to change the look of the composable screen from outside
 * @param searchViewModel - searchViewmodel, viewmodel for fetching and preserving data for this screen.
 */
@Composable
fun SearchScreen(modifier: Modifier = Modifier, searchViewModel: SearchViewModel, nav:NavController) {
    val uiState by searchViewModel.uiState.collectAsState()
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val searchType by searchViewModel.searchType.collectAsState()
    val isConnected by searchViewModel.isConnected.collectAsState()


    val tabs = MediaType.entries
    var selectedTab by remember { mutableStateOf(searchType) }

    LaunchedEffect(key1 = true) {
        searchViewModel.isConnected.collect { online ->
            if (online) {
                searchViewModel.refresh()
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Surface(color = MaterialTheme.colorScheme.surface,) {
            Column(modifier = modifier){
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query -> searchViewModel.updateSearchQuery(query, selectedTab)},
                    singleLine = true,
                    modifier = modifier.fillMaxWidth().padding(10.dp),
                    shape= RoundedCornerShape(50),
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "asdas") },
                    placeholder = { Text("Search...") }
                )

                TabRow(selectedTabIndex = searchType.ordinal) {
                    tabs.forEachIndexed { index, type ->
                        Tab(
                            selected = searchType == type,
                            onClick = dropUnlessResumed{
                                selectedTab = type
                                searchViewModel.updateSearchQuery(searchQuery, selectedTab)
                            },
                            text = { Text(type.title) }
                        )
                    }
                }
            }

        }

        ValidateState(uiState, isInternet = searchViewModel.isConnected.collectAsState().value) { items ->
            if(items.isEmpty()) {
                Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    if(searchQuery.isEmpty()){
                        Image(drawable = R.drawable.write, drawableDark = R.drawable.write_dark)
                        Text("Search by typing")
                    } else {
                        if(!isConnected){
                            Image(drawable = R.drawable.connection, drawableDark = R.drawable.connection_dark)
                            Text("Connect to the Internet.")
                        } else {
                            Image(drawable = R.drawable.empty_shelf, drawableDark = R.drawable.empty_shelf_dark)
                        }
                        Text("Sadly nothing was found")

                    }
                }
            } else {
                LazyColumn {
                 itemsIndexed(items) { index, item ->
                        MediaItem(item = item, openDetail = dropUnlessResumed { nav.navigate("detail/${item.id}/${selectedTab.name}/true") }, mediaType = selectedTab,)
                        // Načitanie dalšej strany vysledkov
                        if (index == items.lastIndex - 5) {
                            searchViewModel.loadMore()
                        }
                    }
                }
            }
        }

    }
}

