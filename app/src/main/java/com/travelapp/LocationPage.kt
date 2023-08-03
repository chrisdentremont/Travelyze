package com.travelapp

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.travelapp.composable.LocationObject
import com.travelapp.ui.theme.BackgroundAccentColor
import com.travelapp.ui.theme.BackgroundColor
import com.travelapp.ui.theme.halcomFamily
import com.travelapp.ui.theme.marsFamily

var showCategoryDialog = mutableStateOf(false)
var selectedCategory = mutableStateOf("")

@Composable
fun LocationPage(
    name: String,
    nav: NavController,
    auth: FirebaseAuth
) {

    var userID = auth.currentUser?.uid.toString()
    var documentReference = Firebase.firestore.collection("users").document(userID)
    var locationSaved by remember {mutableStateOf(false)}
    try{
        locationSaved = currentUser.value.data?.favoriteLocations?.contains(name)!!
    } catch (Exception: java.lang.NullPointerException){

    }

    var selectedLocation = locationList.find { it.Name.equals(name) }

    if(showCategoryDialog.value){
        CategoryDialog(selectedLocation, selectedCategory.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
            TopAppBar(
                title = {
                    androidx.compose.material.Text(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontFamily = marsFamily,
                        text = ""
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if(locationSelected.value){
                                locationSelected.value = false
                                nav.navigate("explore")
                            }else{
                                profileLocationSelected.value = false
                                nav.navigate("profile")
                            }
                        },
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier.size(size = 40.dp),
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    if(locationSaved) {
                        IconButton(
                            onClick = {
                                currentUser.value.data?.favoriteLocations?.remove(name)
                                locationSaved = false
                                documentReference.set(currentUser.value)
                            },
                        ) {
                            Icon(
                                Icons.Filled.Bookmarks,
                                contentDescription = "",
                                modifier = Modifier.size(size = 40.dp),
                                tint = Color.Black
                            )
                        }
                    }else {
                        IconButton(
                            onClick = {
                                currentUser.value.data?.favoriteLocations?.add(name)
                                locationSaved = true
                                documentReference.set(currentUser.value)
                            },
                        ) {
                            Icon(
                                Icons.Outlined.Bookmarks,
                                contentDescription = "",
                                modifier = Modifier.size(size = 40.dp),
                                tint = Color.Black
                            )
                        }
                    }
                },
                backgroundColor = BackgroundAccentColor,
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            ) {
                Text(
                    text = selectedLocation?.Name!!,
                    style = MaterialTheme.typography.headlineLarge,
                    fontFamily = halcomFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(
                    modifier = Modifier.background(color = BackgroundAccentColor)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model =
                            ImageRequest.Builder(LocalContext.current)
                                .decoderFactory(SvgDecoder.Factory())
                                .data(selectedLocation?.Flag)
                                .build(),
                            filterQuality = FilterQuality.None,
                            contentDescription = null,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ){
                        Text(
                            text = selectedLocation?.Description!!,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = halcomFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.padding(start = 15.dp)
            ){
                Text(
                    text = "See more",
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = halcomFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }

            Row(
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 65.dp, start = 5.dp, end = 5.dp)
            ){
                selectedLocation?.Categories!!.forEach {
                    if(it.value.text != null) {

                        var categoryPic = 0
                        when(it.key) {
                            "Climate" -> {categoryPic = R.drawable.climate}
                            "Cuisine" -> {categoryPic = R.drawable.cuisine}
                            "Education" -> {categoryPic = R.drawable.education}
                            "Music" -> {categoryPic = R.drawable.music}
                            "Sports" -> {categoryPic = R.drawable.sports}
                            "Transportation" -> {categoryPic = R.drawable.transportation}
                        }

                        var categoryDisplay = if (it.key == "Transportation") "Transport" else it.key
                        Card(
                            modifier = Modifier
                                .size(width = 150.dp, height = 175.dp)
                                .padding(10.dp)
                                .clickable {
                                    showCategoryDialog.value = true
                                    selectedCategory.value = it.key
                                },
                            shape = RoundedCornerShape(15.dp),
                        ) {
                            Column(){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ){
                                    Box(

                                    ){
                                        Image(
                                            painter = painterResource(id = categoryPic),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier
                                                .categoryShadow(
                                                    blur = 15.dp,
                                                    color = Color.Black,
                                                    cornersRadius = 10.dp,
                                                    offsetY = (-30).dp,
                                                )
                                        )
                                        Text(
                                            text = categoryDisplay,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontFamily = halcomFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(10.dp)
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

@Composable
fun CategoryDialog(
    location: LocationObject?,
    category: String
){
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            showCategoryDialog.value = false
        },
        title = {
            Row(){
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = 40.dp)
                        .padding(end = 10.dp)
                        .clickable {
                            showCategoryDialog.value = false
                        },
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = halcomFamily,
                    fontWeight = FontWeight.Normal,
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ){
                    Text(
                        text = location?.Categories?.get(category)?.text!!,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = halcomFamily,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                var images = location?.Categories?.get(category)?.images
                if(!images.isNullOrEmpty()){
                    images.forEach {
                        AsyncImage(
                            model =
                            ImageRequest.Builder(LocalContext.current)
                                .data("https:${it.key}")
                                .build(),
                            filterQuality = FilterQuality.None,
                            contentDescription = null,
                            modifier = Modifier.size(width = 300.dp, height = 150.dp)
                        )
                        Text(
                            text = it.value,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = halcomFamily,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {

        },
        dismissButton = {

        },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    )
}

fun Modifier.categoryShadow(
    color: Color = Color.Black,
    cornersRadius: Dp = 0.dp,
    spread: Dp = 0.dp,
    blur: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawWithContent {

    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()

    drawIntoCanvas {

        paint.color = color
        paint.isAntiAlias = true
        it.saveLayer(rect, paint)
        it.drawRoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val left = if (offsetX > 0.dp) {
            rect.left + offsetX.toPx()
        } else {
            rect.left
        }
        val top = if (offsetY > 0.dp) {
            rect.top + offsetY.toPx()
        } else {
            rect.top
        }
        val right = if (offsetX < 0.dp) {
            rect.right + offsetX.toPx()
        } else {
            rect.right
        }
        val bottom = if (offsetY < 0.dp) {
            rect.bottom + offsetY.toPx()
        } else {
            rect.bottom
        }
        paint.color = Color.Black
        it.drawRoundRect(
            left = left + spread.toPx() / 2,
            top = top + spread.toPx() / 2,
            right = right - spread.toPx() / 2,
            bottom = bottom - spread.toPx() / 2,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
        frameworkPaint.xfermode = null
        frameworkPaint.maskFilter = null
    }
}