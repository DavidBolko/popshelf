package com.example.popshelf

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.popshelf.presentation.NotificationSystem
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
import android.Manifest


/**
 * The main activity of the Popshelf application.
 *
 * This activity sets the theme and contains the [NavHost] of the application.
 */
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Notifications", "Povolené")
        } else {
            Log.d("Notifications", "Nepovolené")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            val navController = rememberNavController()
            val notif = NotificationSystem(applicationContext)

            PopshelfTheme {
                //Scaffold - Zakladny layout, nejaky topbar, obsah, bottom bar...
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
                            ),
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern = "popshelf://add/{id}/{mediaType}/{shelfId}/{isEdit}"
                                }
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
                            ),
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern = "popshelf://detail/{id}/{mediaType}/{fromShelf}"
                                }
                            )
                        )
                        { backStackEntry ->
                            val viewModel: DetailViewModel = viewModel(
                                factory = DetailViewModel.Factory,
                                viewModelStoreOwner = backStackEntry
                            )

                            DetailScreen(detailViewModel = viewModel, nav = navController)
                        }
                    }
                }

                /*
                Button(onClick = {
                    notif.runTestNotif()
                }) {
                    Text("Test Notification")
                }

                 */


            }

        }
    }
}
