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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

//Definicia route z dokumentácie
//https://developer.android.com/develop/ui/compose/navigation
data class AppRoute(val name: String, val route: String, val icons: List<ImageVector>)

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
                selected = currentDestination?.hierarchy?.any { it.route == item.name } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
