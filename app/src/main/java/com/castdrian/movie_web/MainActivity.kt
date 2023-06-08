package com.castdrian.movie_web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity


/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainWebView = WebView(this.applicationContext)
        val webSettings: WebSettings = mainWebView.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true

        setContentView(mainWebView)
        mainWebView.loadUrl("https://movie-web.app")
    }
}