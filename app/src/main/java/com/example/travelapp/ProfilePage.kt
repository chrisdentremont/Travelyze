package com.example.travelapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
fun Profile(){
    Column(
        Modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Row(Modifier.fillMaxWidth()){
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.LightGray)
                    .border(2.dp, SolidColor(Color.Black), shape = RoundedCornerShape(20.dp))
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)

                ) {
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
        Row(Modifier.fillMaxWidth()){
            Column(Modifier.fillMaxWidth()) {
                Text(text = "Settings",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                )

                Text(text = "\tChange email\n\n\tChange Password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(10.dp))

                Text(text = "\n\tDelete Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(10.dp),
                    color = Color.Red
                )
            }
        }
    }
}
