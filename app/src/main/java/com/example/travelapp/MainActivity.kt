package com.example.travelapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.travelapp.composable.LocationObject
import com.example.travelapp.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
//TODO Change to
val isLoggedIn = mutableStateOf(Firebase.auth.currentUser != null)

var locationList = mutableListOf<LocationObject>()
var locationNames = mutableListOf<String?>()

class MainActivity : ComponentActivity() {



    private lateinit var navController: NavHostController
    companion object {
        val TAG:String = MainActivity::class.java.simpleName
    }

    private val auth by lazy {
        Firebase.auth
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            var getLocationsFromDb = getLocationsAsync().await().await()

            for(document in getLocationsFromDb.documents){
                var current = document.toObject<LocationObject>()
                if (current != null) {
                    locationList.add(current)
                    locationNames.add(current.Name)
                }
            }
        }

        setContent {
            TravelAppTheme {

                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()

                DisposableEffect(systemUiController, useDarkIcons) {
                    // Update all of the system bar colors to be transparent, and use
                    // dark icons if we're in light theme
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )

                    // setStatusBarColor() and setNavigationBarColor() also exist

                    onDispose {}
                }

                Box(
                    modifier = Modifier.background(BackgroundColor)
                ){
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        navController = rememberNavController()

                        NavBar()

                        if (openSignoutDialog.value){
                            SignOutDialog(auth)
                        }

                        if(openDeleteDialog.value){
                            DeleteAccountDialog(auth)
                        }

                        if(openEditDialog.value){
                            EditUsernameDialog()
                        }

                        if(openPicDialog.value){
                            ProfilePicturePicker(auth)
                        }

                        if(openPicSelectDialog.value){
                            ProfilePicSelect(auth)
                        }

                        if(openPicTakenDialog.value){
                            ProfilePicTaken(auth)
                        }

                        if(openAddFriendDialog.value){
                            addFriendDialog()
                        }

                        //If added here the navbar goes away??
//                        if(isAddingFriend.value){
//                            FriendRequests()
//                        }
                    }
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
            Screen.Friends to Icons.Filled.Group,
            Screen.Explore to Icons.Filled.Search,
            Screen.Profile to Icons.Filled.Person,
        )

        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    routeMap.forEach { (key, value) ->
                        BottomNavigationItem(
                            icon = {Icon(value, contentDescription = null, tint = Color.Black)},
                            label = { Text(stringResource(key.resourceId),
                                color = Color.Black,
                                fontFamily = robotoFamily,) },
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
                                    // re-selecting the same item
                                    launchSingleTop = true
                                    // Restore state when re-selecting a previously selected item
                                    restoreState = (navController.graph.findNode(Screen.Register.route) == null)
                                }
                            },
                            modifier = Modifier.background(color = BackgroundColor)
                        )
                    }

                }
            }
        ) {
                innerPadding ->
            NavHost(navController, startDestination = Screen.Explore.route, Modifier.padding(innerPadding)) {
                composable(Screen.Friends.route) {
                    if(isLoggedIn.value){
                        isDrawerOpen.value = false
                        Social()

                        if(isAddingFriend.value){
                            FriendRequests()
                        }
                    }
                    else{
                        isDrawerOpen.value = false
                        Login(auth, navController)
                    }
                }
                composable(Screen.Explore.route) {
                    isDrawerOpen.value = false
                    isAddingFriend.value = false
                    Home()
                }
                composable(Screen.Profile.route) {
                    if (isLoggedIn.value) {
                        isAddingFriend.value = false
                        Profile(auth)
                    }
                    else{
                        isDrawerOpen.value = false
                        Login(auth, navController)
                    }
                }
                composable(Screen.Login.route){
                    isDrawerOpen.value = false
                    Login(auth, navController)
                }
                composable(Screen.Register.route){
                    isDrawerOpen.value = false
                    RegisterForm()
                }
            }
        }


    }

    private fun getLocationsAsync() = GlobalScope.async {
        var fireStore = FirebaseFirestore.getInstance()
        var locationCollection = fireStore.collection("countries")
        locationCollection.get().addOnSuccessListener {
            Log.w("success", "successful")
        }
            .addOnFailureListener {exception ->
                Log.w("Exception", exception)
            }
    }
}

/**
 * The class that contains information about every existing page
 * within the application.
 */
sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Explore : Screen("explore", R.string.explore_name)
    object Friends : Screen("friends", R.string.friends_name)
    object Profile : Screen("profile", R.string.profile_name)
    object Login : Screen("login", R.string.login_name)
    object Register : Screen("register", R.string.register_name)
}


