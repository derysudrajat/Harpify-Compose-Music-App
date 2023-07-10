package id.derysudrajat.harpify.ui.screen.searchscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import id.derysudrajat.harpify.R

@Composable
fun EmptyLoading(
    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize()){
        val loadingAnimationComposition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                R.raw.lottie_listen_anim
            )
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                modifier = Modifier
                    .size(250.dp),
                composition = loadingAnimationComposition,
                iterations = LottieConstants.IterateForever
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Find and Play as you want",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )

        }

    }
}