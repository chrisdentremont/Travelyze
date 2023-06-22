package com.example.travelapp

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
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

@Composable
fun Social(){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(all = 20.dp)
            .verticalScroll(rememberScrollState())
    ){
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "Friends",
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .weight(2f),
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),

            ) {
                Icon(
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = "",
                    modifier = Modifier.size(size = 40.dp)
                    )
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
//                            Image(
//                                painter = rememberAsyncImagePainter("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/f19a8dbd-b9e4-4ce8-9726-5a1412453ce7/dabkw0g-5004586b-8f46-40df-ae0e-9baef2aa6929.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcL2YxOWE4ZGJkLWI5ZTQtNGNlOC05NzI2LTVhMTQxMjQ1M2NlN1wvZGFia3cwZy01MDA0NTg2Yi04ZjQ2LTQwZGYtYWUwZS05YmFlZjJhYTY5MjkucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.22JifnHYgz5tBl9jIweAjmPxLjujBdDl-rVYBjR1p14"),
//                                contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .size(75.dp)
//                                    .clip(CircleShape)
//                                    .border(5.dp, Color.White, CircleShape)
//                            )

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
//                            Image(
//                                painter = rememberAsyncImagePainter("https://pm1.narvii.com/6345/ed3b88240fbbf8b1b89a2d29980a19d6f8834b1a_hq.jpg"),
//                                contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .size(75.dp)
//                                    .clip(CircleShape)
//                                    .border(5.dp, Color.White, CircleShape)
//                            )
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
//                            Image(
//                                painter = rememberAsyncImagePainter("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/f19a8dbd-b9e4-4ce8-9726-5a1412453ce7/dabldy4-9446bc17-de1f-4429-bb33-8f2762c8e7ac.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcL2YxOWE4ZGJkLWI5ZTQtNGNlOC05NzI2LTVhMTQxMjQ1M2NlN1wvZGFibGR5NC05NDQ2YmMxNy1kZTFmLTQ0MjktYmIzMy04ZjI3NjJjOGU3YWMucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.wbNGcuCGuxmYCndImvx3gyEb4FZ9dE0Mv8IlBhzEy-4"),
//                                contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .size(75.dp)
//                                    .clip(CircleShape)
//                                    .border(5.dp, Color.White, CircleShape)
//                            )
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
//                            Image(
//                                painter = rememberAsyncImagePainter("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/f19a8dbd-b9e4-4ce8-9726-5a1412453ce7/dabheg6-f7233d4d-8ef7-4611-bed8-b65ad9b93060.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcL2YxOWE4ZGJkLWI5ZTQtNGNlOC05NzI2LTVhMTQxMjQ1M2NlN1wvZGFiaGVnNi1mNzIzM2Q0ZC04ZWY3LTQ2MTEtYmVkOC1iNjVhZDliOTMwNjAucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.D_drECvO2Lur5t0MrGn5HpHHWSwaTmr5Ik_kbu9pmqY"),
//                                contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .size(75.dp)
//                                    .clip(CircleShape)
//                                    .border(5.dp, Color.White, CircleShape)
//                            )
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