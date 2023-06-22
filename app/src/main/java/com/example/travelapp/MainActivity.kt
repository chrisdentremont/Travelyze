package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Diversity1
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.travelapp.ui.theme.TravelAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TravelAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Login()
                    if(openApp.value){
                        OpenApp()
                    }
                    //NavBar()
                }
            }
        }
    }
}


/**
 * The class that contains information about every existing page
 * within the application.
 */
sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home_name)
    object Friends : Screen("friends", R.string.friends_name)
    object Settings : Screen("profile", R.string.profile_name)
}

/**
 * The composable function that represents the tabs at the bottom
 * of the screen and provides functionality to them.
 */
@Composable
fun NavBar(){
    val routeMap = mapOf(
        Screen.Friends to Icons.Outlined.Diversity1,
        Screen.Home to Icons.Filled.Search,
        Screen.Settings to Icons.Filled.Person
    )

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                routeMap.forEach { (key, value) ->
                    BottomNavigationItem(
                        icon = {Icon(value, contentDescription = null)},
                        label = { Text(stringResource(key.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == key.route } == true,
                        onClick = {
                            navController.navigate(key.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
            innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Friends.route) {Social()}
            composable(Screen.Home.route) {Home()}
            composable(Screen.Settings.route) { Profile() }
        }
    }
}

@Composable
fun OpenApp() {
    Home()
    NavBar()
}