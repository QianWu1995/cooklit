package com.macrohard.cooklit.model.activities

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

import com.macrohard.cooklit.R

class RecipeActivity : AppCompatActivity() {

    var webview: WebView
    //public ImageView imageView;
    private val progressBar: ProgressDialog? = null
    private var uri: String? = null
    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        webview = findViewById<View>(R.id.webview)
        //imageView = (ImageView) findViewById(R.id.imageView);
        uri = intent.getStringExtra("uri")
        val settings = webview.settings
        settings.javaScriptEnabled = true
        webview.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        Log.d("new webview", uri)

        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.i(TAG, "Processing webview url click...")
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "Finished loading URL: $url")
            }

        }

        webview.loadUrl(uri)
    }

    companion object {
        private val TAG = "Webview loading"
    }

}