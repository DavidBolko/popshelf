package com.example.popshelf.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.popshelf.R
import com.example.popshelf.presentation.components.Image


/***
 * Class which represents individual UI states.
 */
sealed class UIState<out T> {
    /*** Class which represents loading state of UI for example when fetching data.*/
    object Loading : UIState<Nothing>()
    /*** Class which represents success state, after fetching when data are available.*/
    data class Success<T>(val data: T) : UIState<T>()
    /*** Class which represents success state, after fetching when something goes wrong.
     * @param message - message which can be displayed as error message.
     * */
    data class Error(val message: String) : UIState<Nothing>()
}

/*** Composable function which decides if data are available, if yes it takes them a put them inside the passed
 * composable to show it on the screen.
 * @author David Bolko
 * @param uiState - uiState passed from viewmodel which preserves actual uistate, when success data are rendered on screen.
 * @param modifier - modifier for ability to change the look of the composable screen from outside
 * @param isInternet - boolean for deciding if internet is available or not
 * @param composable - lambda composable which is going to be rendered when UI state succeeded.
 */
@Composable
fun <T> ValidateState(uiState: UIState<T>, modifier: Modifier = Modifier, isInternet: Boolean = true, composable: @Composable (T) -> Unit){
    when (uiState) {
        is UIState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(drawable = R.drawable.search, drawable_dark = R.drawable.search_dark)
            }
        }

        is UIState.Success -> {
            composable(uiState.data)
        }

        is UIState.Error -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if(isInternet){
                    Image(drawable = R.drawable.error, drawable_dark = R.drawable.error_dark)
                    Text(text = "Vyskytla sa chyba", color = Color.Red, fontSize = 18.sp)
                } else {
                    Image(drawable = R.drawable.connection, drawable_dark = R.drawable.connection_dark)
                    Text(text = "No connection to the internet", color = Color.Red, fontSize = 18.sp)
                }
            }
        }

    }
}