package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.app.multiplatformcomposeapplication.ui.theme.MultiplatformComposeApplicationTheme
import com.multiplatformcomposeapplication.common.MarkdownWithRichText

@OptIn(ExperimentalComposeApi::class)
@Composable
public fun App() {
    MultiplatformComposeApplicationTheme {
        Scaffold {
            MarkdownWithRichText(
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}