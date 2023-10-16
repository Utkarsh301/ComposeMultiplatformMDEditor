package com.multiplatformcomposeapplication.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.window.Window
import com.multiplatformcomposeapplication.common.App
import org.jetbrains.skiko.wasm.onWasmReady

public fun main() {
    onWasmReady {
        Window("MultiplatformComposeApplication") {
            MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
                App()
            }
        }
    }
}
