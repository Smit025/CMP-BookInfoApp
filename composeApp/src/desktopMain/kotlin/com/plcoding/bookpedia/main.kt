package com.plcoding.bookpedia

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import okhttp3.OkHttp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CMP-Bookpedia",
    ) {
        App(engine = remember { io.ktor.client.engine.okhttp.OkHttp.create() })
    }
}