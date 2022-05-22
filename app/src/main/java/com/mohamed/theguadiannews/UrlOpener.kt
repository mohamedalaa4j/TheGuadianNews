package com.mohamed.theguadiannews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.mohamed.theguadiannews.databinding.ActivityUrlOpnerBinding

class UrlOpener : AppCompatActivity() {
    private var binding : ActivityUrlOpnerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUrlOpnerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.webViewClient = WebViewClient()

        var url:String = intent.getStringExtra("url").toString()

        binding?.webView?.loadUrl(url)

        binding?.backButton?.setOnClickListener { onBackPressed() }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}