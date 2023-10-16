package com.multiplatformcomposeapplication.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

public actual fun getPlatformName(): String {
    return "MultiplatformComposeApplication"
}

@Preview
@Composable
public fun UIShow() {
   App()
}