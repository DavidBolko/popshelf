package com.example.popshelf.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

//Definicia route z dokumentácie
//https://developer.android.com/develop/ui/compose/navigation
/**
 * Represents a navigation route in the application.
 * @property name display name of the route (example: "Home").
 * @property route identifier navigation path or identifier (example: "home_screen").
 * @property icons A list of icons representing the unselected and selected states.
 * Typically contains two icons: one for the default state and one for the active state.
 */
data class AppRoute(val name: String, val route: String, val icons: List<ImageVector>)


/**
 * Composable function which takes navigation controller and renders the navigation bar of the application.
 * @param navController - navigation controller to allow navigation from this screen or to the next.
 */
@Composable
fun NavigationBar(navController: NavHostController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val appRoutes = listOf(
        AppRoute("Home", "home", listOf(Icons.Outlined.Home, Icons.Filled.Home)),
        AppRoute("Search", "search", listOf(Icons.Outlined.Search, Icons.Filled.Search)),
        ///AppRoute("Settings", "home", listOf(Icons.Outlined.Settings, Icons.Filled.Settings))
    )
    androidx.compose.material3.NavigationBar {
        appRoutes.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (currentDestination?.route == appRoutes[index].route) appRoutes[index].icons.last() else appRoutes[index].icons.first(),
                        contentDescription = item.name
                    )
                },
                label = { Text(item.name) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
