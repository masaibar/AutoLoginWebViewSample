package com.masaibar.inputeventcatchtest

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val URL_TOP = "https://yoyaku.sports.metro.tokyo.jp/sp/"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        web_view.apply {
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("!!!", url)
                    if (url != URL_TOP) {
                        button.visibility = View.GONE
                    } else {
                        button.visibility = View.VISIBLE
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
            }
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            web_view.loadUrl(URL_TOP)
        }

        button.setOnClickListener {
            web_view.evaluateJavascript(
                "javascript:" +
                        "document.getElementsByName('userId')[0].value = 'id';" +
                        "document.getElementsByName('password')[0].value = 'pw';" +
                        "document.getElementsByName('login')[0].click();",
                null
            )
        }
    }
}
