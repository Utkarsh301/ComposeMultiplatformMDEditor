package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay

public object WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        WelcomeContent()
    }
}

@Composable
public fun WelcomeContent() {

    var progress by remember {
        mutableStateOf(0f)
    }
    val navigator = LocalNavigator.currentOrThrow
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            progress = i.toFloat().div(100)
            delay(10)
            if (i == 100) {
                navigator.push(MarkdownEditorScreen)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to the MD Editor")
        Spacer(Modifier.height(16.dp))
        CircularProgressIndicator(
            progress = progress,
            strokeCap = StrokeCap.Round

        )
    }

}



