package com.example.travelapp

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.travelapp.composable.Drawer
import com.example.travelapp.composable.TopBar
import com.example.travelapp.ui.theme.Alabaster
import kotlinx.coroutines.launch

val isDrawerOpen = mutableStateOf(false)

@Composable
fun Profile(){
    Surface(color = Color.White){
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        fun openDrawer()  {
            scope.launch {
                drawerState.open()
            }
        }

        if(isDrawerOpen.value){
            openDrawer()
        } else {

        }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
            ModalDrawer(
                drawerState = drawerState,
                gesturesEnabled = drawerState.isOpen,
                drawerContent = {
                    //TODO Drawer Shows up behind everything??
                    Drawer(
                        onDestinationClicked = { route ->
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            ){
                //ModalDrawerSample()
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(
                title = "",
                buttonIcon = Icons.Filled.Settings,
                onButtonClicked = {
                    isDrawerOpen.value = !isDrawerOpen.value
                }
            )

            //
            // Profile Box
            //
            Row(Modifier.fillMaxWidth()){
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, top = 10.dp, end = 30.dp, bottom = 30.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Alabaster)
                        .border(2.dp, SolidColor(Color.Black), shape = RoundedCornerShape(20.dp))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)

                    ) {
                        //TODO Create a method of editing the profile page
                        Image(
                            painter = rememberAsyncImagePainter("https://www.theshirtlist.com/wp-content/uploads/2018/12/Rowlet.jpg"),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(5.dp, Color.White, CircleShape)
                        )
                        Text(text = "Gabriel Madeira",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        )


                    }

                    Row(Modifier.fillMaxWidth()) {
                        Divider(startIndent = 0.dp, thickness = 2.dp, color = Color.Black)
                    }

                    Column( Modifier
                        .fillMaxWidth(),
                    ) {
                        Text(text = "Favorites",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                        )

                        Text(text = "\tPortland, OR, USA\n\n\tHartford, CT, USA\n\n\tStockholm, SE\n\n\tEncino, CA, USA\n\n\tSão Paulo, Brazil",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(10.dp))
                    }
                }
            }


//        Row(Modifier.fillMaxWidth()){
//            Column(Modifier.fillMaxWidth()) {
//                Text(text = "Settings",
//                    fontSize = 30.sp,
//                    fontWeight = FontWeight.Normal,
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
//                )
////TODO Create buttons for these actions
//                Text(text = "\tChange email\n\n\tChange Password",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Normal,
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.padding(10.dp))
//
//                Text(text = "\n\tDelete Account",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Normal,
//                    textAlign = TextAlign.Start,
//                    modifier = Modifier.padding(10.dp),
//                    color = Color.Red
//                )
//            }
//        }
        }
    }
}

@Composable
fun ModalDrawerSample() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
        ModalDrawer(
            drawerState = drawerState,
            drawerContent = {
                Column {
                    Text("Text in Drawer")
                    Button(onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    }) {
                        Text("Close Drawer")
                    }
                }
            },
            content = {
                Column {
                    Text("Text in Bodycontext")
                    Button(onClick = {

                        scope.launch {
                            drawerState.open()
                        }

                    }) {
                        Text("Click to open")
                    }
                }
            }
        )
    }
}
