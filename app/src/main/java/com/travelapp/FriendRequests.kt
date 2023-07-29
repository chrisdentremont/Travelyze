package com.travelapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.travelapp.ui.theme.Alabaster
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.TuftsBlue
import com.travelapp.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendRequests() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Incoming", "Outgoing")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        Scaffold(
            topBar = {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            modifier = Modifier.background(color = Color.DarkGray),
                            icon = {
                                when (index) {
                                    0 -> Icon(Icons.Filled.Inbox, "")
                                    1 -> Icon(Icons.Filled.ForwardToInbox, "")
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
fun IncomingRequestsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser.value.requests?.incomingFriendRequests?.size == 0) {
            item {
                Text(
                    text = "Incoming Requests",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            for (incomingUser in currentUser.value.requests?.incomingFriendRequests!!) {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Column(
                                    Modifier.fillMaxWidth(0.78f)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        //TODO Use values from persons account
                                        Image(
                                            painter = rememberAsyncImagePainter("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/be718605-eac9-40a7-bccd-dea055256f78/d5d27jb-f8843464-3ce2-4419-be3d-4b1210ca52a4.jpg/v1/fill/w_877,h_620,q_75,strp/mudkip_by_star_soul_d5d27jb-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjIwIiwicGF0aCI6IlwvZlwvYmU3MTg2MDUtZWFjOS00MGE3LWJjY2QtZGVhMDU1MjU2Zjc4XC9kNWQyN2piLWY4ODQzNDY0LTNjZTItNDQxOS1iZTNkLTRiMTIxMGNhNTJhNC5qcGciLCJ3aWR0aCI6Ijw9ODc3In1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.6U--8zmMblPQSkaSs_gy1ATKl6euB83wHSn--e_36os"),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(75.dp)
                                                .clip(CircleShape)
                                                .border(5.dp, Color.White, CircleShape)
                                        )

                                        Text(
                                            "Chris D'Entremont",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }

                                Column(
                                    Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        IconButton(
                                            modifier = Modifier.padding(end = 15.dp),
                                            onClick = { /*TODO Accept Friend Request*/ },
                                        ) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = "",
                                                modifier = Modifier.size(size = 40.dp)
                                            )
                                        }
                                        IconButton(
                                            modifier = Modifier.padding(end = 10.dp),
                                            onClick = { /*TODO Reject Friend Request*/ },
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "",
                                                modifier = Modifier.size(size = 40.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OutgoingRequestsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser.value.requests?.outgoingFriendRequests?.size == 0) {
            item {
                Text(
                    text = "Outgoing Requests",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        } else {
            for (outgoingUser in currentUser.value.requests?.outgoingFriendRequests!!) {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(Alabaster)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Column(
                                    Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        //TODO Use values from persons account
                                        Image(
                                            painter = rememberAsyncImagePainter("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/be718605-eac9-40a7-bccd-dea055256f78/d5d27jb-f8843464-3ce2-4419-be3d-4b1210ca52a4.jpg/v1/fill/w_877,h_620,q_75,strp/mudkip_by_star_soul_d5d27jb-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjIwIiwicGF0aCI6IlwvZlwvYmU3MTg2MDUtZWFjOS00MGE3LWJjY2QtZGVhMDU1MjU2Zjc4XC9kNWQyN2piLWY4ODQzNDY0LTNjZTItNDQxOS1iZTNkLTRiMTIxMGNhNTJhNC5qcGciLCJ3aWR0aCI6Ijw9ODc3In1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.6U--8zmMblPQSkaSs_gy1ATKl6euB83wHSn--e_36os"),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(75.dp)
                                                .clip(CircleShape)
                                                .border(5.dp, Color.White, CircleShape)
                                        )

                                        Text(
                                            "Chris D'Entremont",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }

                                Column(
                                    Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        IconButton(
                                            modifier = Modifier.padding(end = 9.dp),
                                            onClick = { /*TODO Reject Friend Request*/ },
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "",
                                                modifier = Modifier.size(size = 40.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}