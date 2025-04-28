package com.example.popshelf.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.popshelf.presentation.MediaType
import com.example.popshelf.presentation.components.MediaItem
import com.example.popshelf.presentation.validateState
import com.example.popshelf.presentation.viewmodels.SearchViewModel

@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: SearchViewModel, nav:NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchType by viewModel.searchType.collectAsState()

    val tabs = MediaType.entries
    var selectedTab by remember { mutableStateOf(searchType) }

    Column(modifier = Modifier) {
        Surface(color = MaterialTheme.colorScheme.surface,) {
            Column(modifier= Modifier){
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query -> viewModel.updateSearchQuery(query, selectedTab)},
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
                            onClick = {
                                selectedTab = type
                                viewModel.updateSearchQuery(searchQuery, selectedTab)
                            },
                            text = { Text(type.title) }
                        )
                    }
                }
            }

        }

        validateState(uiState) { items->
            LazyColumn {
                items(items) { item ->
                    MediaItem(item, openDetail = { nav.navigate("detail/${item.id}/${selectedTab.name}/${true}") }, selectedTab)
                }
            }
        }
    }
}

