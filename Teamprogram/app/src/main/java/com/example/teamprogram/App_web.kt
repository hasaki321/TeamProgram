package com.example.teamprogram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class App_web : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_web)
        supportActionBar?.hide()
        val url = intent.getStringExtra("url")
        val webView = findViewById<WebView>(R.id.app_webview)
        webView.settings.javaScriptEnabled=true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url!!)
    }
}