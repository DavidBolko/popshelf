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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.popshelf.presentation.components.NavigationBar
import com.example.popshelf.presentation.screens.AddScreen
import com.example.popshelf.presentation.screens.DetailScreen
import com.example.popshelf.presentation.screens.HomeScreen
import com.example.popshelf.presentation.screens.SearchScreen
import com.example.popshelf.presentation.screens.ShelfScreen
import com.example.popshelf.presentation.viewmodels.AddItemViewModel
import com.example.popshelf.presentation.viewmodels.AddShelfViewModel
import com.example.popshelf.presentation.viewmodels.DetailViewModel
import com.example.popshelf.presentation.viewmodels.HomeViewModel
import com.example.popshelf.presentation.viewmodels.SearchViewModel
import com.example.popshelf.presentation.viewmodels.ShelfViewModel
import com.example.popshelf.ui.theme.PopshelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStackEntry?.destination?.route

            PopshelfTheme {
                //Scaffold - Zakladny layout, ponúka nejaky topbar, obsah, bottom bar...
                Scaffold(bottomBar = { NavigationBar(navController) }) { padding ->
                    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(padding)) {
                        composable("home"){
                            val viewModel: HomeViewModel = viewModel(
                                factory = HomeViewModel.Factory,
                            )
                            val addShelfViewModel: AddShelfViewModel = viewModel(
                                factory = AddShelfViewModel.Factory,
                            )
                            HomeScreen(viewModel=viewModel, addShelfViewModel = addShelfViewModel, nav = navController)
                        }
                        composable("search"){
                            val viewModel: SearchViewModel = viewModel(
                                factory = SearchViewModel.Factory,
                            )
                            SearchScreen(viewModel = viewModel, nav = navController)
                        }
                        composable(
                            "shelf/{id}/{name}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.StringType },
                                navArgument("name") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val viewModel: ShelfViewModel = viewModel(
                                factory = ShelfViewModel.Factory,
                                viewModelStoreOwner = backStackEntry
                            )

                            ShelfScreen(viewModel = viewModel, nav = navController)
                        }
                        composable(
                            "add/{id}/{mediaType}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.StringType },
                                navArgument("mediaType") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val viewModel: AddItemViewModel = viewModel(
                                factory = AddItemViewModel.Factory,
                                viewModelStoreOwner = backStackEntry
                            )

                            AddScreen(viewModel = viewModel, nav = navController)
                        }
                        composable(
                            "detail/{id}/{mediaType}/{fromShelf}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.StringType },
                                navArgument("mediaType") { type = NavType.StringType },
                                navArgument("fromShelf") { type = NavType.BoolType }
                            )
                        ) { backStackEntry ->
                            val viewModel: DetailViewModel = viewModel(
                                factory = DetailViewModel.Factory,
                                viewModelStoreOwner = backStackEntry
                            )

                            DetailScreen(viewModel = viewModel, nav = navController)
                        }
                    }
                }
            }
        }
    }
}