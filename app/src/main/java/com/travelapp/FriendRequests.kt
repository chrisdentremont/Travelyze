package com.travelapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.travelapp.ui.theme.*
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.TuftsBlue


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendRequests(){
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Incoming", "Outgoing")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        Scaffold (
            topBar = {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            modifier = Modifier.background(color = TuftsBlue),
                            icon = {
                                when (index){
                                    0 -> Icon(Icons.Filled.Inbox, "")
                                    1 -> Icon(Icons.Filled.ForwardToInbox,"")
                                }
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { openAddFriendDialog.value = true },
                    backgroundColor = TuftsBlue,
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )
            },
            content = {
                when (tabIndex) {
                    0 -> IncomingRequestsScreen()
                    1 -> OutgoingRequestsScreen()
                }
            }
        )
    }
}

@Composable
fun IncomingRequestsScreen(){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Incoming Requests",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OutgoingRequestsScreen(){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = "Outgoing Requests",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}