package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.app.multiplatformcomposeapplication.ui.theme.MultiplatformComposeApplicationTheme

@Composable
public fun App() {
    MultiplatformComposeApplicationTheme {
        Scaffold {
            Navigator(
                screen = WelcomeScreen,
                onBackPressed = { currentScreen ->
                    (currentScreen !is MarkdownEditorScreen)

                }
            )
        }
    }

}