package com.example.travelapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.travelapp.composable.TopBar
import com.example.travelapp.ui.theme.Alabaster

@Composable
fun Social_LoggedIn(){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            TopBar(
                title = "Friends",
                buttonIcon = Icons.Outlined.AddCircle,
                onButtonClicked = {
                    /*TODO Implement friend addition window*/
                }
            )
//            Text(
//                text = "Friends",
//                fontSize = 30.sp,
//                fontWeight = FontWeight.Normal,
//                modifier = Modifier
//                    .padding(bottom = 20.dp)
//                    .weight(2f),
//            )

//            IconButton(
//                onClick = { /*TODO Implement friend addition window*/ },
//                modifier = Modifier
//                    .padding(start = 10.dp)
//                    .weight(1f),
//
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.AddCircle,
//                    contentDescription = "",
//                    modifier = Modifier.size(size = 40.dp)
//                    )
//            }
        }

        //TODO Improve the friend page UI and display information in a better way
        Row(){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = 10.dp,
                            backgroundColor = Alabaster
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
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
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier.border(2.dp,SolidColor(Color.Black),shape = RoundedCornerShape(5.dp))
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about Paris, France!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "Paris is a European city located in France...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = 10.dp,
                            backgroundColor = Alabaster
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter("https://www.theshirtlist.com/wp-content/uploads/2018/12/Rowlet.jpg"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .border(5.dp, Color.White, CircleShape)
                                    )
                                    Text(
                                        "Gabriel Madeira",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier.border(2.dp,SolidColor(Color.Black),shape = RoundedCornerShape(5.dp))
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about London, England!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "London is a European city located in England...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = 10.dp,
                            backgroundColor = Alabaster
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter("https://media.istockphoto.com/id/458297017/photo/hello-kitty-pattern.jpg?s=612x612&w=0&k=20&c=IvVb5qyDu9_E_M0LCDPGFcpSIzr016ZHm9ZrXANQhQs="),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .border(5.dp, Color.White, CircleShape)
                                    )
                                    Text(
                                        "Mo Merchant",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier.border(2.dp,SolidColor(Color.Black),shape = RoundedCornerShape(5.dp))
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about Las Vegas, Nevada!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "Las Vegas is an American city located in Nevada...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Column(){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = 10.dp,
                            backgroundColor = Alabaster
                        ){
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter("https://nintendosoup.com/wp-content/uploads/2022/03/Chloe_Eevee.jpg"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(75.dp)
                                            .clip(CircleShape)
                                            .border(5.dp, Color.White, CircleShape)
                                    )
                                    Text(
                                        "Ayla Hebert",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }

                                Column(
                                    Modifier.border(2.dp,SolidColor(Color.Black),shape = RoundedCornerShape(5.dp))
                                        .padding(8.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ){
                                        Text(
                                            text = "I read about São Paulo, Brazil!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(start = 10.dp))
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                    ){
                                        Text(
                                            text = "São Paulo is a South American city located in Brazil...",
                                            modifier = Modifier.padding(start = 10.dp, bottom = 15.dp),
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