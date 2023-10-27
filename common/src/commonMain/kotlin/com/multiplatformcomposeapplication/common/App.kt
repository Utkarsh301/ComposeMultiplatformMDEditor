package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.app.multiplatformcomposeapplication.ui.theme.MultiplatformComposeApplicationTheme

@OptIn(ExperimentalComposeApi::class)
@Composable
public fun App() {
    MultiplatformComposeApplicationTheme {
        Scaffold {
            MyMarkdownEditor(
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}