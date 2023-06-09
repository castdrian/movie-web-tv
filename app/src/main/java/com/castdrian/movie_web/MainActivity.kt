package com.castdrian.movie_web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainWebView = findViewById<WebView>(R.id.webView)
        val webSettings: WebSettings = mainWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true

        mainWebView.loadUrl("https://movie-web.app")
    }
}