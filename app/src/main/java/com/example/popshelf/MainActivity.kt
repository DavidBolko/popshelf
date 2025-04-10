package com.example.popshelf

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.popshelf.presentation.components.NavigationBar
import com.example.popshelf.presentation.screens.HomeScreen
import com.example.popshelf.presentation.screens.SearchScreen
import com.example.popshelf.presentation.viewmodels.SearchViewModel
import com.example.popshelf.ui.theme.PopshelfTheme


class MainActivity : ComponentActivity() {
    private lateinit var searchViewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as PopshelfApplication).appContainer
        searchViewModel = appContainer.searchViewModelFactory.create();

        setContent {
            val navController = rememberNavController()
            PopshelfTheme {
                //Scaffold - Zakladny layout, ponúka nejaky topbar, obsah, bottom bar...
                Scaffold(bottomBar = { NavigationBar(navController) }) { padding ->
                    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
                        composable("home"){
                            HomeScreen()
                        }
                        composable("search"){
                            SearchScreen(viewModel = searchViewModel)
                        }

                    }
                }
            }
        }
    }
}