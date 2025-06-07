package com.example.popshelf.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.popshelf.R

/**
 * Wrapper composable function based around Async image from Coil library.
 * @param modifier modifier for ability to change the look of the composable screen from outside
 * @param drawable actual drawable image from resource manager which can be defined by R.drawable.*  (light theme)
 * @param drawableDark actual drawable image from resource manager which can be defined by R.drawable.* for dark theme.
 * @param size ability to change size of the image (Warning: if images are not same sized this parameter will not be consistent)
 */
@Composable
fun Image(modifier: Modifier = Modifier, drawable: Int, drawableDark: Int? = null, size: Int = 80){
    val imageLoader = ImageLoader.Builder(LocalContext.current).components { add(GifDecoder.Factory()) }.build()
    val image = if (isSystemInDarkTheme() && drawableDark != null) drawableDark else drawable

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(image).build(),
        contentDescription = stringResource(R.string.loading),
        imageLoader = imageLoader,
        modifier = modifier.size(size.dp)
    )
}