package com.multiplatformcomposeapplication.common

import androidx.compose.runtime.Composable

public actual fun getPlatformName(): String {
    return "MultiplatformComposeApplication"
}

@Composable
public fun UIShow() {
    App()
}