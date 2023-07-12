package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.example.travelapp.ui.theme.Aero
import com.example.travelapp.ui.theme.TravelAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    companion object {
        val TAG:String = MainActivity::class.java.simpleName
    }

    private val auth by lazy {
        Firebase.auth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TravelAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    navController = rememberNavController()



                    Home()
                    NavBar()
                }
            }
        }
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
            Screen.Profile to Icons.Filled.Person
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
                            },
                            modifier = Modifier.background(color = Aero)
                        )
                    }

                }
            }
        ) {
                innerPadding ->
            NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
                composable(Screen.Friends.route) {
                    if(isLoggedIn.value){
                        Social_LoggedIn()
                    }
                    else{
                        Social_Default()
                    }
                }
                composable(Screen.Home.route) {Home()}
                composable(Screen.Profile.route) {
                    if (isLoggedIn.value) {
                        Profile()
                    }
                    else{
                        Login(auth)
                    }

                    if(isRegistering.value){
                        RegisterForm()
                    }
                }
                composable(Screen.Register.route){
                    if(isLoggedIn.value){
                        Login(auth)
                    }
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
    object Profile : Screen("profile", R.string.profile_name)
    object Login : Screen("login", R.string.login_name)
    object Register : Screen("register", R.string.register_name)
}


