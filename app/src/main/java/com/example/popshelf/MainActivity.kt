package com.example.popshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.popshelf.presentation.components.NavigationBar
import com.example.popshelf.presentation.screens.AddScreen
import com.example.popshelf.presentation.screens.DetailScreen
import com.example.popshelf.presentation.screens.HomeScreen
import com.example.popshelf.presentation.screens.SearchScreen
import com.example.popshelf.presentation.screens.ShelfScreen
import com.example.popshelf.presentation.viewmodels.AddEditItemViewModel
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
                            HomeScreen(homeViewModel=viewModel, addShelfViewModel = addShelfViewModel, nav = navController)
                        }
                        composable("search"){
                            val viewModel: SearchViewModel = viewModel(
                                factory = SearchViewModel.Factory,
                            )
                            SearchScreen(searchViewModel = viewModel, nav = navController)
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

                            ShelfScreen(shelfViewModel = viewModel, nav = navController)
                        }
                        composable(
                            "add/{id}/{mediaType}/{shelfId}/{isEdit}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.StringType },
                                navArgument("mediaType") { type = NavType.StringType },
                                navArgument("shelfId") { type = NavType.IntType },
                                navArgument("isEdit") { type = NavType.BoolType },
                            )
                        ) { backStackEntry ->
                            val viewModel: AddEditItemViewModel = viewModel(
                                factory = AddEditItemViewModel.Factory,
                                viewModelStoreOwner = backStackEntry
                            )

                            AddScreen(addEditItemViewModel = viewModel, nav = navController)
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

                            DetailScreen(detailViewModel = viewModel, nav = navController)
                        }
                    }
                }
            }
        }
    }
}